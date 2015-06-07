package com.rawpixil.eclipse.launchpad.core;

/**
 *
 * @author Willy du Preez
 *
 */
public interface IExtendedLaunchConfigurationListener {

	/**
	 * The given extended launch configuration has been created. Listeners should
	 * not perform long running operations in the callback mmethod and should
	 * return as quickly as possible.
	 *
	 * @param configuration the newly created launch configuration
	 */
	public void extendedLaunchConfigurationAdded(IExtendedLaunchConfiguration configuration);

	/**
	 * The given extended launch configuration has changed in some way.
	 *
	 * @param configuration the launch configuration that has changed
	 */
	public void extendedLaunchConfigurationChanged(IExtendedLaunchConfiguration configuration);

	/**
	 * The given extended launch configuration has been deleted.
	 * <p>
	 * The launch configuration no longer exists. Data stored in the wrapped launch
	 * configuration can no longer be accessed, however handle-only attributes of
	 * the launch configuration can be retrieved.
	 * </p>
	 *
	 * @param configuration the deleted launch configuration
	 */
	public void extendedLaunchConfigurationRemoved(IExtendedLaunchConfiguration configuration);

}
