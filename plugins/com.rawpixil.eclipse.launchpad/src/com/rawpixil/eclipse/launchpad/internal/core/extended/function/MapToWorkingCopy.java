package com.rawpixil.eclipse.launchpad.internal.core.extended.function;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfigurationWorkingCopy;
import com.rawpixil.eclipse.launchpad.core.IFunction;

public class MapToWorkingCopy implements IFunction<IExtendedLaunchConfiguration, IExtendedLaunchConfigurationWorkingCopy> {

	@Override
	public IExtendedLaunchConfigurationWorkingCopy apply(IExtendedLaunchConfiguration t) {
		return t.getWorkingCopy();
	}

}
