package com.rawpixil.eclipse.launchpad;

import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.rawpixil.eclipse.launchpad.core.IPluginState;
import com.rawpixil.eclipse.launchpad.internal.core.PluginStateProvider;
import com.rawpixil.eclipse.launchpad.internal.core.extended.LaunchPadProvider;

/**
 * There is one instance of the launch pad plug-in available from
 * {@link LaunchPadPlugin#getDefault()}.
 *
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be sub-classed by clients.
 *
 * @author Willy du Preez
 *
 */
public class LaunchPadPlugin extends AbstractUIPlugin {

	/**
	 * Unique identifier constant (value <code>"com.rawpixil.eclipse.launchpad"</code>)
	 * for the Eclipse Launch Pad plug-in.
	 */
	public static final String PLUGIN_ID = "com.rawpixil.eclipse.launchpad"; //$NON-NLS-1$

	public static final int EXTENDED_LAUNCH_CONFIGURATION_NOTIFICATION_ERROR = 1;
	public static final int EXTENDED_LAUNCHES_NOTIFICATION_ERROR = 2;

	private static LaunchPadPlugin instance;

	/**
	 * Returns the shared instance.
	 *
	 * @return the shared instance
	 */
	public static LaunchPadPlugin getDefault() {
		return instance;
	}

	public LaunchPadPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;

		IPluginState state = PluginStateProvider.INSTANCE.get();
		ISavedState saved = ResourcesPlugin.getWorkspace().addSaveParticipant(PLUGIN_ID, state);
		state.restore(saved);

		// Instantiate all providers.
		LaunchPadProvider.INSTANCE.get();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		instance = null;
		super.stop(context);

		if (ResourcesPlugin.getWorkspace() != null) {
			ResourcesPlugin.getWorkspace().removeSaveParticipant(PLUGIN_ID);
		}
	}

}
