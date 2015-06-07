package com.rawpixil.eclipse.launchpad.internal.core.extended;

import com.rawpixil.eclipse.launchpad.core.IExtendedLauncher;

public enum ExtendedLauncherProvider {

	INSTANCE;

	private IExtendedLauncher launcher;

	private ExtendedLauncherProvider() {
		this.launcher = new ExtendedLauncher();
	}

	public IExtendedLauncher get() {
		return this.launcher;
	}

}
