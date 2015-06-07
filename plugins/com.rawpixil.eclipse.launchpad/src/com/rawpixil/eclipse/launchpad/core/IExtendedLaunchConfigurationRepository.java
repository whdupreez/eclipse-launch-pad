package com.rawpixil.eclipse.launchpad.core;

import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;

import com.rawpixil.eclipse.launchpad.internal.util.Optional;

public interface IExtendedLaunchConfigurationRepository {

	List<IExtendedLaunchConfiguration> list();
	List<IExtendedLaunchConfiguration> list(IPredicate<IExtendedLaunchConfiguration> filter);

	void add(IExtendedLaunchConfiguration extended);
	void remove(IExtendedLaunchConfiguration configuration);

	void save(IExtendedLaunchConfigurationWorkingCopy extended);

	Optional<IExtendedLaunchConfiguration> findByLaunchConfiguration(ILaunchConfiguration config);

	void addExtendedLaunchConfigurationListener(IExtendedLaunchConfigurationListener listener);
	void removeExtendedLaunchConfigurationListener(IExtendedLaunchConfigurationListener listener);

}
