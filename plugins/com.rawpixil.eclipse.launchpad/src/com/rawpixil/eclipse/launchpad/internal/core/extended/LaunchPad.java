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
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.ui.DebugUITools;

import com.rawpixil.eclipse.launchpad.LaunchPadPlugin;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunch;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationRepository;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchesListener;
import com.rawpixil.eclipse.launchpad.core.ILaunchPad;
import com.rawpixil.eclipse.launchpad.internal.message.Messages;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;
import com.rawpixil.eclipse.launchpad.internal.util.Dialogs;
import com.rawpixil.eclipse.launchpad.internal.util.Log;
import com.rawpixil.eclipse.launchpad.internal.util.Optional;

/**
 * Acts as a bridge that attaches an extended launch configuration to the launch
 * event and propagates the event.
 *
 * @author Willy du Preez
 *
 */
// TODO Remove listeners registered to framework
public class LaunchPad implements ILaunchPad {

	private enum NotificationType { ADDED, CHANGED, TERMINATED, REMOVED };

	private IExtendedLaunchConfigurationRepository repository;
	private ListenerList listeners;
	private List<IExtendedLaunch> register;

	public LaunchPad() {
		this.repository = ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get();
		this.listeners = new ListenerList();
		this.register = Collections.synchronizedList(new ArrayList<IExtendedLaunch>());

		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(new LaunchesListener());
	}

	@Override
	public void launch(IExtendedLaunchConfiguration extended) {
		Assert.notNull(extended, "Cannot launch null Extended Launch Configuration");
		Optional<String> mode = extended.getDefaultLaunchMode();
		if (mode.isPresent()) {
			Log.log("Launching in mode: " + mode.get());
			DebugUITools.launch(extended.getLaunchConfiguration(), mode.get());
		}
		else {
			Dialogs.error(Messages.extended_launcher_error_cannot_launch_title,
					Messages.extended_launcher_error_cannot_launch_description);
		}
	}

	@Override
	public List<IExtendedLaunch> getExtendedLaunches(IExtendedLaunchConfiguration extended) {
		return Collections.unmodifiableList(this.findExtendedLaunches(extended));
	}

	private void handleLaunchesAdded(ILaunch[] launches) {
		List<IExtendedLaunch> added = new ArrayList<IExtendedLaunch>();
		synchronized (this.register) {
			for (ILaunch launch : launches) {
				final Optional<IExtendedLaunchConfiguration> config = LaunchPad.this.repository
						.findByLaunchConfiguration(launch.getLaunchConfiguration());

				if (config.isPresent()) {
					Log.log("Config matched to launch!");
					ExtendedLaunch extended = new ExtendedLaunch(config.get(), launch);
					added.add(extended);
				}
				else {
					Log.log("No config found for launch: " + launch.getLaunchConfiguration().getName());
				}
			}
			this.register.addAll(added);
		}
		this.getNotifier().notify(added, NotificationType.ADDED);
	}

	private void handleLaunchesChanged(ILaunch[] launches) {
		List<IExtendedLaunch> changed = this.findExtendedLaunches(launches);
		if (!changed.isEmpty()) {
			this.getNotifier().notify(changed, NotificationType.CHANGED);
		}
	}

	private void handleLaunchesTerminated(ILaunch[] launches) {
		List<IExtendedLaunch> terminated = this.findExtendedLaunches(launches);
		if (!terminated.isEmpty()) {
			this.getNotifier().notify(terminated, NotificationType.TERMINATED);
		}
	}

	private void handleLaunchesRemoved(ILaunch[] launches) {
		List<IExtendedLaunch> removed;
		synchronized (this.register) {
			removed = this.findExtendedLaunches(launches);
			this.register.removeAll(removed);
		}
		if (!removed.isEmpty()) {
			this.getNotifier().notify(removed, NotificationType.REMOVED);
		}
	}

	private Optional<IExtendedLaunch> findExtendedLaunch(ILaunch launch) {
		synchronized (this.register) {
			Iterator<IExtendedLaunch> i = this.register.iterator();
			while (i.hasNext()) {
				IExtendedLaunch extended = i.next();
				if (extended.getLaunch().equals(launch)) {
					return Optional.of(extended);
				}
			}
			return Optional.empty();
		}
	}

	private List<IExtendedLaunch> findExtendedLaunches(ILaunch[] launches) {
		synchronized (this.register) {
			List<IExtendedLaunch> found = new ArrayList<IExtendedLaunch>();
			for (ILaunch launch : launches) {
				Optional<IExtendedLaunch> extended = this.findExtendedLaunch(launch);
				if (extended.isPresent()) {
					found.add(extended.get());
				}
			}
			return found;
		}
	}

	private List<IExtendedLaunch> findExtendedLaunches(IExtendedLaunchConfiguration extended) {
		synchronized (this.register) {
			List<IExtendedLaunch> found = new ArrayList<IExtendedLaunch>();
			Iterator<IExtendedLaunch> i = this.register.iterator();
			while (i.hasNext()) {
				IExtendedLaunch launch = i.next();
				if (launch.getExtendedLaunchConfiguration().equals(extended)) {
					found.add(launch);
				}
			}
			return found;
		}
	}

	@Override
	public void addExtendedLaunchesListener(IExtendedLaunchesListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeExtendedLaunchesListener(IExtendedLaunchesListener listener) {
		this.listeners.remove(listener);
	}

	private ExtendedLaunchesNotifier getNotifier() {
		return new ExtendedLaunchesNotifier();
	}

	private class LaunchesListener implements ILaunchesListener2 {

		@Override
		public void launchesAdded(ILaunch[] launches) {
			Log.log("LaunchPad.LaunchesListener.launchesAdded");
			LaunchPad.this.handleLaunchesAdded(launches);
		}

		@Override
		public void launchesChanged(ILaunch[] launches) {
			Log.log("LaunchPad.LaunchesListener.launchesChanged");
			LaunchPad.this.handleLaunchesChanged(launches);
		}

		@Override
		public void launchesTerminated(ILaunch[] launches) {
			Log.log("LaunchPad.LaunchesListener.launchesTerminated");
			LaunchPad.this.handleLaunchesTerminated(launches);
		}

		@Override
		public void launchesRemoved(ILaunch[] launches) {
			Log.log("LaunchPad.LaunchesListener.launchesRemoved");
			LaunchPad.this.handleLaunchesRemoved(launches);
		}

	}

	/**
	 * Notifies an extended launch configuration listener in a safe runnable to
	 * handle exceptions.
	 */
	private class ExtendedLaunchesNotifier implements ISafeRunnable {

		private NotificationType type;
		private List<IExtendedLaunch> launches;
		private IExtendedLaunchesListener listener;

		@Override
		public void handleException(Throwable exception) {
			IStatus status = new Status(
					IStatus.ERROR,
					LaunchPadPlugin.PLUGIN_ID,
					LaunchPadPlugin.EXTENDED_LAUNCHES_NOTIFICATION_ERROR,
					"An exception occurred during extended launches notification.", //$NON-NLS-1$
					exception);

			LaunchPadPlugin.getDefault().getLog().log(status);
		}

		public void notify(List<IExtendedLaunch> launches, NotificationType type) {
			this.launches = Collections.unmodifiableList(launches);
			this.type = type;
			Object[] listeners = LaunchPad.this.listeners.getListeners();
			for (Object listener : listeners) {
				this.listener = ((IExtendedLaunchesListener) listener);
				SafeRunner.run(this);
			}
			this.launches = null;
			this.type = null;
			this.listener = null;
		}

		@Override
		public void run() throws Exception {
			switch (this.type) {
			case ADDED:
				this.listener.extendedLaunchesAdded(this.launches);
				break;
			case CHANGED:
				this.listener.extendedLaunchesChanged(this.launches);
				break;
			case TERMINATED:
				this.listener.extendedLaunchesTerminated(this.launches);
				break;
			case REMOVED:
				this.listener.extendedLaunchesRemoved(this.launches);
				break;
			default:
				Log.log("Usupported notifaction type");
				break;
			}
		}
	}

}
