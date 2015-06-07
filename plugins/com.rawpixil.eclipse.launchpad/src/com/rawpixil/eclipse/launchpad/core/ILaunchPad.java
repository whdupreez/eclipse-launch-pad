package com.rawpixil.eclipse.launchpad.core;

import java.util.List;

public interface ILaunchPad {

	void launch(IExtendedLaunchConfiguration extended);

	/**
	 * Returns a list of currently registered launches for the extended
	 * launch configuration.
	 *
	 * @return currently registered launches
	 */
	List<IExtendedLaunch> getExtendedLaunches(IExtendedLaunchConfiguration extended);

	void addExtendedLaunchesListener(IExtendedLaunchesListener listener);
	void removeExtendedLaunchesListener(IExtendedLaunchesListener listener);

}
