package com.rawpixil.eclipse.launchpad.core;

import java.util.List;

import org.eclipse.debug.core.model.IProcess;

public interface ILaunchPad {

	void launch(IExtendedLaunchConfiguration extended);

	/**
	 * Returns a list of currently registered launches for the extended
	 * launch configuration. Terminated launches are included.
	 *
	 * @return currently registered launches
	 */
	List<IExtendedLaunch> getExtendedLaunches(IExtendedLaunchConfiguration extended);

	/**
	 * Returns a list of currently registered launches for the extended
	 * launch configuration.
	 *
	 * @param true if terminated launches should be included, false otherwise
	 * @return currently registered launches
	 */
	List<IExtendedLaunch> getExtendedLaunches(IExtendedLaunchConfiguration extended, boolean includeTerminated);

	/**
	 * Returns a list of processes for the extended launch configuration.
	 * Note: Only processes that have not terminated and belong to launches
	 * that have not terminated are included in the list.
	 *
	 * @param extended the configuration
	 * @return the active processes belonging to active launches
	 */
	List<IProcess> getProcesses(IExtendedLaunchConfiguration extended);

	void addExtendedLaunchesListener(IExtendedLaunchesListener listener);
	void removeExtendedLaunchesListener(IExtendedLaunchesListener listener);

}
