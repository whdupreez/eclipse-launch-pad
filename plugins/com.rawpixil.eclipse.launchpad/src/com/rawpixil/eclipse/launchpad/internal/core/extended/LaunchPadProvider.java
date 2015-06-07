package com.rawpixil.eclipse.launchpad.internal.core.extended;

import com.rawpixil.eclipse.launchpad.core.ILaunchPad;

public enum LaunchPadProvider {

	INSTANCE;

	private ILaunchPad launchpad;

	private LaunchPadProvider() {
		this.launchpad = new LaunchPad();
	}

	public ILaunchPad get() {
		return this.launchpad;
	}

}
