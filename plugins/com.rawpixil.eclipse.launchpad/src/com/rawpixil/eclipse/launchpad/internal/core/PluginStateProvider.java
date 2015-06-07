package com.rawpixil.eclipse.launchpad.internal.core;

import com.rawpixil.eclipse.launchpad.core.IPluginState;

public enum PluginStateProvider {

	INSTANCE;

	private IPluginState state;

	private PluginStateProvider() {
		this.state = new PluginState();
	}

	public IPluginState get() {
		return this.state;
	}

}
