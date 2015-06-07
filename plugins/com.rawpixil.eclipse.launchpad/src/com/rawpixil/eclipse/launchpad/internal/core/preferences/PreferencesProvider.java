package com.rawpixil.eclipse.launchpad.internal.core.preferences;

import com.rawpixil.eclipse.launchpad.core.IPreferences;

public enum PreferencesProvider {

	INSTANCE;

	private IPreferences preferences;

	private PreferencesProvider() {
		this.preferences = new Preferences();
	}

	public IPreferences get() {
		return this.preferences;
	}

}
