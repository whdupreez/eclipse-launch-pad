package com.rawpixil.eclipse.launchpad.core;

public interface IExtendedLauncher {

	void launch(IExtendedLaunchConfiguration extended);

	void addExtendedLaunchesListener(IExtendedLaunchesListener listener);
	void removeExtendedLaunchesListener(IExtendedLaunchesListener listener);

}
