package com.rawpixil.eclipse.launchpad.internal.core.extended.filter;

import com.rawpixil.eclipse.launchpad.core.IExtendedLaunchConfiguration;
import com.rawpixil.eclipse.launchpad.internal.util.functional.AbstractPredicate;

public class Favorites extends AbstractPredicate<IExtendedLaunchConfiguration> {

	@Override
	public boolean test(IExtendedLaunchConfiguration t) {
		return t.isFavorite();
	}

}
