package com.rawpixil.eclipse.launchpad.core;

import java.util.List;

public interface IExtendedLaunchesListener {

	/**
	 * Notifies this listener that the specified launches have been added.
	 *
	 * @param launches
	 *            the newly added launch objects
	 */
	public void extendedLaunchesAdded(List<IExtendedLaunch> launches);

	/**
	 * Notifies this listener that the specified launches have changed. For
	 * example, a process or debug target has been added to a launch.
	 *
	 * @param launches
	 *            the changed launch object
	 */
	public void extendedLaunchesChanged(List<IExtendedLaunch> launches);

	/**
	 * Notification that the given launches have terminated.
	 *
	 * @param launches
	 *            the launches that have terminated
	 */
	public void extendedLaunchesTerminated(List<IExtendedLaunch> launches);

	/**
	 * Notifies this listener that the specified launches have been removed.
	 *
	 * @param launches
	 *            the removed launch objects
	 */
	public void extendedLaunchesRemoved(List<IExtendedLaunch> launches);

}
