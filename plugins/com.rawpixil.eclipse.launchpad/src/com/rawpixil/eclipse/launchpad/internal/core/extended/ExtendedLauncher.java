package com.rawpixil.eclipse.launchpad.internal.core.extended;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
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
import com.rawpixil.eclipse.launchpad.core.IExtendedLauncher;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchesListener;
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
public class ExtendedLauncher implements IExtendedLauncher {

	private enum NotificationType { ADDED, CHANGED, TERMINATED, REMOVED };

	private IExtendedLaunchConfigurationRepository repository;
	private List<IExtendedLaunchesListener> listeners;

	public ExtendedLauncher() {
		this.repository = ExtendedLaunchConfigurationRepositoryProvider.INSTANCE.get();
		this.listeners = Collections.synchronizedList(new ArrayList<IExtendedLaunchesListener>());
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
			Log.log("ExtendedLauncher.launchesAdded");
			this.propagateNotification(launches, NotificationType.ADDED);
		}

		@Override
		public void launchesChanged(ILaunch[] launches) {
			Log.log("ExtendedLauncher.launchesChanged");
			this.propagateNotification(launches, NotificationType.CHANGED);
		}

		@Override
		public void launchesTerminated(ILaunch[] launches) {
			Log.log("ExtendedLauncher.launchesTerminated");
			this.propagateNotification(launches, NotificationType.TERMINATED);
		}

		@Override
		public void launchesRemoved(ILaunch[] launches) {
			Log.log("ExtendedLauncher.launchesRemoved");
			this.propagateNotification(launches, NotificationType.REMOVED);
		}

		private void propagateNotification(ILaunch[] launches, NotificationType type) {
			synchronized (ExtendedLauncher.this.listeners) {
				List<IExtendedLaunch> matched = new ArrayList<IExtendedLaunch>();
				for (ILaunch added : launches) {
					final Optional<IExtendedLaunchConfiguration> config = ExtendedLauncher.this.repository
							.findByLaunchConfiguration(added.getLaunchConfiguration());

					if (config.isPresent()) {
						Log.log("Config matched to launch!");
						ExtendedLaunch extended = new ExtendedLaunch(config.get(), added);
						matched.add(extended);
					}
					else {
						Log.log("No config :(");
					}
				}
				// Must be notified in the synchronized block.
				ExtendedLauncher.this.getNotifier().notify(matched, type);
			}
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
//			synchronized (ExtendedLauncher.this.listeners) {
				if (ExtendedLauncher.this.listeners.size() > 0) {
					Iterator<IExtendedLaunchesListener> i = ExtendedLauncher.this.listeners.iterator();
					while (i.hasNext()) {
						this.listener = i.next();
						SafeRunner.run(this);
					}
				}
//			}
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
