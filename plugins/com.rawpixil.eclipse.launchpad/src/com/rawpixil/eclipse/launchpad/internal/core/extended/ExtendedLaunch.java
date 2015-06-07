package com.rawpixil.eclipse.launchpad.internal.core.extended;

import org.eclipse.debug.core.ILaunch;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunch;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;

public class ExtendedLaunch implements IExtendedLaunch {

	private IExtendedLaunchConfiguration config;
	private ILaunch launch;

	ExtendedLaunch(IExtendedLaunchConfiguration config, ILaunch launch) {
		Assert.notNull(config, "Extended launch configuration cannot be null");
		Assert.notNull(launch, "Launch cannot be null");
		this.config = config;
		this.launch = launch;
	}

	@Override
	public IExtendedLaunchConfiguration getExtendedLaunchConfiguration() {
		return this.config;
	}

	@Override
	public ILaunch getLaunch() {
		return this.launch;
	}

}
