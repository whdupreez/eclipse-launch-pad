package com.rawpixil.eclipse.launchpad.core;

import org.eclipse.jface.util.IPropertyChangeListener;

public interface IPreferences {

	boolean isEnbaled(IPreference preference);
	void setEnabled(IPreference preference, boolean enabled);

	void addPropertyChangeListener(IPropertyChangeListener lstPreferencesChange);
	void removePropertyChangeListener(IPropertyChangeListener lstPreferencesChange);

}
