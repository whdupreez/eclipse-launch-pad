package com.rawpixil.eclipse.launchpad.internal.ui.presentation;

import com.rawpixil.eclipse.launchpad.internal.ui.IPresentation;

public enum LaunchPadModelPresentationProvider {

	INSTANCE;

	private LaunchPadModelPresentation presentation;

	private LaunchPadModelPresentationProvider() {
		this.presentation = new LaunchPadModelPresentation();
	}

	public IPresentation<Object> get() {
		return this.presentation;
	}

	public void dispose() {
		this.presentation = null;
	}

}
