package com.rawpixil.eclipse.launchpad.internal.core.extended;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;

import com.rawpixil.eclipse.launchpad.LaunchPadPlugin;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationListener;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationRepository;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.core.IPredicate;
import com.rawpixil.eclipse.launchpad.internal.core.extended.ExtendedLaunchConfiguration.ExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.ELF;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

/**
 *
 * @author Willy du Preez
 *
 */
public class ExtendedLaunchConfigurationRepository implements IExtendedLaunchConfigurationRepository {

	private enum NotificationType { ADDED, CHANGED, REMOVED };

	private List<IExtendedLaunchConfiguration> storage;
	private ListenerList listeners;

	public ExtendedLaunchConfigurationRepository() {
		this.storage = Collections.synchronizedList(new ArrayList<IExtendedLaunchConfiguration>());
		this.listeners = new ListenerList();

		ELF.registerLaunchConfigurationListener(new LaunchConfigurationListener());
	}

	@Override
	public void add(IExtendedLaunchConfiguration extended) {
		Assert.notNull(extended, "Cannot add null Extended Launch Configuration");

		if (this.storage.add(extended)) {
			this.getNotifier().notify(extended, NotificationType.ADDED);
		}
	}

	@Override
	public void save(IExtendedLaunchConfigurationWorkingCopy copy) {
		Assert.notNull(copy, "Cannot update null Extended Launch Configuration");
		Optional<IExtendedLaunchConfiguration> saved = ((ExtendedLaunchConfigurationWorkingCopy) copy).save();
		if (saved.isPresent()) {
			this.getNotifier().notify(saved.get(), NotificationType.CHANGED);
		}
	}

	@Override
	public void remove(IExtendedLaunchConfiguration extended) {
		Assert.notNull(extended, "Cannot remove null Extended Launch Configuration");

		if (this.storage.remove(extended)) {
			this.getNotifier().notify(extended, NotificationType.REMOVED);
		}
	}

	@Override
	public List<IExtendedLaunchConfiguration> list() {
		return Collections.unmodifiableList(new ArrayList<IExtendedLaunchConfiguration>(this.storage));
	}

	@Override
	public List<IExtendedLaunchConfiguration> list(IPredicate<IExtendedLaunchConfiguration> filter) {
		synchronized (this.storage) {
			List<IExtendedLaunchConfiguration> matched = new ArrayList<IExtendedLaunchConfiguration>();
			Iterator<IExtendedLaunchConfiguration> i = this.storage.iterator();
			while (i.hasNext()) {
				IExtendedLaunchConfiguration extended = i.next();
				if (filter.test(extended)) {
					matched.add(extended);
				}
			}
			return Collections.unmodifiableList(matched);
		}
	}

	@Override
	public Optional<IExtendedLaunchConfiguration> findByLaunchConfiguration(ILaunchConfiguration config) {
		synchronized (this.storage) {
			Iterator<IExtendedLaunchConfiguration> i = this.storage.iterator();
			while (i.hasNext()) {
				IExtendedLaunchConfiguration extended = i.next();
				if (extended.getLaunchConfiguration().equals(config)) {
					return Optional.of(extended);
				}
			}
			return Optional.empty();
		}
	}

	@Override
	public void addExtendedLaunchConfigurationListener(IExtendedLaunchConfigurationListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeExtendedLaunchConfigurationListener(IExtendedLaunchConfigurationListener listener) {
		this.listeners.remove(listener);
	}

	private ConfigurationNotifier getNotifier() {
		return new ConfigurationNotifier();
	}

	private class LaunchConfigurationListener implements ILaunchConfigurationListener {

		@Override
		public void launchConfigurationAdded(ILaunchConfiguration added) {
			// Note: Rename is handled as a move operation.
			Optional<ILaunchConfiguration> moved = ELF.getLaunchedConfigurationMovedFrom(added);
			if (moved.isPresent()) {
				Optional<IExtendedLaunchConfiguration> extended =
						ExtendedLaunchConfigurationRepository.this.findByLaunchConfiguration(moved.get());
				if (extended.isPresent()) {
					IExtendedLaunchConfigurationWorkingCopy copy = extended.get().getWorkingCopy();
					copy.setLaunchConfiguration(added);
					ExtendedLaunchConfigurationRepository.this.save(copy);
				}
				else {
					IExtendedLaunchConfiguration missing = new ExtendedLaunchConfiguration(added);
					ExtendedLaunchConfigurationRepository.this.add(missing);
				}
			}
			else {
				IExtendedLaunchConfiguration extended = new ExtendedLaunchConfiguration(added);
				ExtendedLaunchConfigurationRepository.this.add(extended);
			}
		}

		@Override
		public void launchConfigurationChanged(ILaunchConfiguration config) {
		}

		@Override
		public void launchConfigurationRemoved(ILaunchConfiguration removed) {
			// Note: Removed will not be present if the add
			// operation handled a move or rename.
			Optional<IExtendedLaunchConfiguration> extended =
					ExtendedLaunchConfigurationRepository.this.findByLaunchConfiguration(removed);
			if (extended.isPresent()) {
				ExtendedLaunchConfigurationRepository.this.remove(extended.get());
			}
		}

	}

	/**
	 * Notifies an extended launch configuration listener in a safe runnable to
	 * handle exceptions.
	 */
	private class ConfigurationNotifier implements ISafeRunnable {

		private NotificationType type;
		private IExtendedLaunchConfiguration extended;
		private IExtendedLaunchConfigurationListener listener;

		@Override
		public void handleException(Throwable exception) {
			IStatus status = new Status(
					IStatus.ERROR,
					LaunchPadPlugin.PLUGIN_ID,
					LaunchPadPlugin.EXTENDED_LAUNCH_CONFIGURATION_NOTIFICATION_ERROR,
					"An exception occurred during extended launch configuration change notification.", //$NON-NLS-1$
					exception);

			LaunchPadPlugin.getDefault().getLog().log(status);
		}

		public void notify(IExtendedLaunchConfiguration extended, NotificationType type) {
			this.extended = extended;
			this.type = type;
			Object[] listeners = ExtendedLaunchConfigurationRepository.this.listeners.getListeners();
			for (Object object : listeners) {
				this.listener = ((IExtendedLaunchConfigurationListener) object);
				SafeRunner.run(this);
			}
			this.extended = null;
			this.type = null;
			this.listener = null;
		}

		@Override
		public void run() throws Exception {
			switch (this.type) {
			case ADDED:
				this.listener.extendedLaunchConfigurationAdded(this.extended);
				break;
			case REMOVED:
				this.listener.extendedLaunchConfigurationRemoved(this.extended);
				break;
			case CHANGED:
				this.listener.extendedLaunchConfigurationChanged(this.extended);
				break;
			default:
				break;
			}
		}
	}

}
