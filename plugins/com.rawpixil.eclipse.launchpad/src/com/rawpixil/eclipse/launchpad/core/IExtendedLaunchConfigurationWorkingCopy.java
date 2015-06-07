package com.rawpixil.eclipse.launchpad.core;

import org.eclipse.debug.core.ILaunchConfiguration;

import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public interface IExtendedLaunchConfigurationWorkingCopy extends IExtendedLaunchConfiguration {

	boolean isDirty();

	void setLaunchConfiguration(ILaunchConfiguration config);
	void setFavorite(boolean favorite);
	void setDefaultLaunchMode(Optional<String> mode);

}
