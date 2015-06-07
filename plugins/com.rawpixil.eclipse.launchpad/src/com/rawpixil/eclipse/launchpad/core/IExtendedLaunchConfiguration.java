package com.rawpixil.eclipse.launchpad.core;

import org.eclipse.debug.core.ILaunchConfiguration;

import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public interface IExtendedLaunchConfiguration {

	boolean exists();

	IExtendedLaunchConfigurationWorkingCopy getWorkingCopy();

	ILaunchConfiguration getLaunchConfiguration();
	boolean isFavorite();
	// TODO This should not be optional
	Optional<String> getDefaultLaunchMode();

}
