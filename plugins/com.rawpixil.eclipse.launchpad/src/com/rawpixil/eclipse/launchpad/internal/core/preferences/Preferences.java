package com.rawpixil.eclipse.launchpad.internal.core.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.rawpixil.eclipse.launchpad.LaunchPadPlugin;
import com.rawpixil.eclipse.launchpad.core.IPreference;
import com.rawpixil.eclipse.launchpad.core.IPreferences;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;

// TODO Preferences change listener ...
public class Preferences implements IPreferences {

	@Override
	public boolean isEnbaled(IPreference preference) {
		Assert.notNull(preference, "Preference cannot be null");
		return this.store().getBoolean(preference.getName());
	}

	@Override
	public void setEnabled(IPreference preference, boolean enabled) {
		Assert.notNull(preference, "Preference cannot be null");
		this.store().setValue(preference.getName(), enabled);
	}

	private IPreferenceStore store() {
		return LaunchPadPlugin.getDefault().getPreferenceStore();
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		Assert.notNull(listener, "Property change listener cannot be null");
		this.store().addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		Assert.notNull(listener, "Property change listener cannot be null");
		this.store().removePropertyChangeListener(listener);
	}

}