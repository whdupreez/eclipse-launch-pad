package com.rawpixil.eclipse.launchpad.internal.ui.launchpad;

import org.eclipse.jface.action.Action;

import com.rawpixil.eclipse.launchpad.core.IPreferences;
import com.rawpixil.eclipse.launchpad.internal.core.preferences.Preference;
import com.rawpixil.eclipse.launchpad.internal.core.preferences.PreferencesProvider;
import com.rawpixil.eclipse.launchpad.internal.util.Images;

public class FilterFavoritesAction extends Action {

	// TODO Static factory methods right on this class ... do it
	public FilterFavoritesAction() {
		super("filter favorites", Images.getImageDescriptor(Images.ICON_FAVORITES));
		this.setChecked(PreferencesProvider.INSTANCE.get().isEnbaled(Preference.LAUNCHER_VIEW_FAVORITES_FILTER));
	}

	@Override
	public void run() {
		IPreferences preferences = PreferencesProvider.INSTANCE.get();
		boolean filter = preferences.isEnbaled(Preference.LAUNCHER_VIEW_FAVORITES_FILTER);
		preferences.setEnabled(Preference.LAUNCHER_VIEW_FAVORITES_FILTER, !filter);
	}

}
