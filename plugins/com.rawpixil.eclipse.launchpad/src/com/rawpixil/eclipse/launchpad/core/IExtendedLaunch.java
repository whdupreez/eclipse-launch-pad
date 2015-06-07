package com.rawpixil.eclipse.launchpad.core;

import org.eclipse.debug.core.ILaunch;

public interface IExtendedLaunch {

	IExtendedLaunchConfiguration getExtendedLaunchConfiguration();
	ILaunch getLaunch();

}
