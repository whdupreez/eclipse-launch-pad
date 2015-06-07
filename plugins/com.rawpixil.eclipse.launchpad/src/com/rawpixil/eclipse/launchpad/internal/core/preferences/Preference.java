package com.rawpixil.eclipse.launchpad.internal.core.preferences;

import com.rawpixil.eclipse.launchpad.core.IPreference;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;

public enum Preference implements IPreference {

	LAUNCHER_VIEW_FAVORITES_FILTER("com.rawpixil.eclipse.launchpad.preferences.launchpad_view.favorites_filter");

	private String name;

	private Preference(String name) {
		Assert.notNull(name, "Name cannot be null");
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
