package com.rawpixil.eclipse.launchpad.internal.util.comparator;

import org.eclipse.debug.ui.ILaunchGroup;

import com.rawpixil.eclipse.launchpad.internal.util.Assert;

/**
 * Categorizes launch groups by category name, then by launch group mode.
 *
 * @author Willy du Preez
 *
 */
public class LaunchGroupCategoryComparator extends LaunchGroupModeComparator {

	private NullSafeComparator<String> nullSafe;

	public LaunchGroupCategoryComparator() {
		this.nullSafe = Comparators.nullSafeStringComparator();
	}

	@Override
	public int compare(ILaunchGroup o1, ILaunchGroup o2) {
		Assert.notNull(o1, "Configuration element cannot be null.");
		Assert.notNull(o2, "Configuration element cannot be null.");

		int result = this.nullSafe.compare(o1.getCategory(), o2.getCategory());

		return result == 0 ? super.compare(o1, o2) : result;
	}

}
