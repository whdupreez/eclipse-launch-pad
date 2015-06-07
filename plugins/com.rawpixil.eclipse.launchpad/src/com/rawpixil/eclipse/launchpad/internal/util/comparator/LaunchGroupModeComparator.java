package com.rawpixil.eclipse.launchpad.internal.util.comparator;

import java.util.Comparator;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchGroup;

import com.rawpixil.eclipse.launchpad.internal.util.Assert;

/**
 * Categorizes launch groups by launch group mode, giving preferences to
 * Eclipse's built-in launch modes.
 *
 * @author Willy du Preez
 *
 */
public class LaunchGroupModeComparator implements Comparator<ILaunchGroup> {

	private NullSafeComparator<String> nullSafe;

	public LaunchGroupModeComparator() {
		this.nullSafe = Comparators.nullSafeStringComparator();
	}

	@Override
	public int compare(ILaunchGroup o1, ILaunchGroup o2) {
		Assert.notNull(o1, "Configuration element cannot be null.");
		Assert.notNull(o2, "Configuration element cannot be null.");

		String o1Mode = o1.getMode();
		String o2Mode = o2.getMode();

		int o1Ranking = this.rank(o1Mode);
		int o2Ranking = this.rank(o2Mode);

		if (o1Ranking > o2Ranking) {
			return Integer.MAX_VALUE;
		}
		else if (o1Ranking < o2Ranking) {
			return Integer.MIN_VALUE;
		}
		else if (o1Ranking != 0 && o1Ranking == o2Ranking) {
			return 0;
		}
		else {
			return this.nullSafe.compare(o1.getLabel(), o2.getLabel());
		}
	}

	private int rank(String mode) {
		if (ILaunchManager.RUN_MODE.equals(mode)) {
			return 3;
		}
		else if (ILaunchManager.DEBUG_MODE.equals(mode)) {
			return 2;
		}
		else if (ILaunchManager.PROFILE_MODE.equals(mode)) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
