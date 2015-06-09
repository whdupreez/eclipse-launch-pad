package com.rawpixil.eclipse.launchpad.internal.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchMode;
import org.eclipse.debug.core.ILaunchesListener;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchGroup;

/**
 * Eclipse Launching Framework utility methods.
 *
 * @author Willy du Preez
 *
 */
public class ELF {

	/**
	 * Returns all registered launch group extensions.
	 *
	 * @see DebugUITools#getLaunchGroups()
	 *
	 * @return all registered launch group extensions
	 */
	public static ILaunchGroup getLaunchGroup(String identifier) {
		List<ILaunchGroup> groups = ELF.getLaunchGroups();
		for (ILaunchGroup group : groups) {
			if (group.getIdentifier().equals(identifier)) {
				return group;
			}
		}
		return null;
	}

	/**
	 * Returns all registered launch group extensions.
	 *
	 * @see DebugUITools#getLaunchGroups()
	 *
	 * @return all registered launch group extensions
	 */
	public static List<ILaunchGroup> getLaunchGroups() {
		ILaunchGroup[] array = DebugUITools.getLaunchGroups();

		List<ILaunchGroup> list = new ArrayList<ILaunchGroup>(array.length);
		Collections.addAll(list, array);
		return list;
	}

	/**
	 * Returns the launch group that the given launch configuration belongs to, for the specified
	 * mode. Note that the configuration may not belong to a launch group.
	 *
	 * @param configuration the launch configuration
	 * @param mode the mode
	 * @return the optional launch group the given launch configuration belongs to
	 */
	public static Optional<ILaunchGroup> getLaunchGroup(ILaunchConfiguration configuration, String mode) {
		return Optional.ofNullable(DebugUITools.getLaunchGroup(configuration, mode));
	}

	/**
	 * Returns all registered launch modes.
	 *
	 * @see ILaunchManager#getLaunchModes()
	 *
	 * @return all registered launch modes
	 */
	public static List<ILaunchMode> getLaunchModes() {
		ILaunchMode[] array = DebugPlugin.getDefault().getLaunchManager().getLaunchModes();

		List<ILaunchMode> list = new ArrayList<ILaunchMode>(array.length);
		Collections.addAll(list, array);
		return list;
	}

	/**
	 * Returns all launch configurations defined in the workspace.
	 *
	 * @return all launch configurations defined in the workspace
	 */
	public static List<ILaunchConfiguration> getLaunchConfigurations() throws CoreException {
		ILaunchConfiguration[] array = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations();

		List<ILaunchConfiguration> list = new ArrayList<ILaunchConfiguration>(array.length);
		Collections.addAll(list, array);
		return list;
	}

	/**
	 * Registers the listener with the {@link ILaunchManager}.
	 *
	 * @see ILaunchManager#addLaunchConfigurationListener(ILaunchesListener)
	 *
	 * @param listener the listener to register
	 */
	public static void addLaunchConfigurationListener(ILaunchConfigurationListener listener) {
		DebugPlugin.getDefault().getLaunchManager().addLaunchConfigurationListener(listener);;
	}

	/**
	 * Unregisters the listener with the {@link ILaunchManager}.
	 *
	 * @see ILaunchManager#removeLaunchConfigurationListener(ILaunchesListener)
	 *
	 * @param listener the listener to remove
	 */
	public static void removeLaunchConfigurationListener(ILaunchConfigurationListener listener) {
		DebugPlugin.getDefault().getLaunchManager().removeLaunchConfigurationListener(listener);;
	}

	/**
	 * Returns the renamed or moved launch configuration, and null if the add notification was due
	 * to a create operation.
	 *
	 * @see ILaunchManager#getMovedFrom(ILaunchConfiguration)
	 *
	 * @param config a launch configuration for which an add notification is being broadcast
	 * @param the renamed or moved launch configuration
	 */
	public static Optional<ILaunchConfiguration> getLaunchedConfigurationMovedFrom(ILaunchConfiguration config) {
		return Optional.ofNullable(DebugPlugin.getDefault().getLaunchManager().getMovedFrom(config));
	}

	public static boolean isExistingLaunchConfigurationName(String name) {
		try {
			return DebugPlugin.getDefault().getLaunchManager().isExistingLaunchConfigurationName(name);
		} catch (CoreException e) {
			return false;
		}
	}

	public static boolean isValidLaunchConfigurationName(String name) {
		try {
			return DebugPlugin.getDefault().getLaunchManager().isValidLaunchConfigurationName(name);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

}
