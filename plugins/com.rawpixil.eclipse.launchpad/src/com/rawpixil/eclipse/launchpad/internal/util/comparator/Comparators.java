package com.rawpixil.eclipse.launchpad.internal.util.comparator;

public final class Comparators {

	private static final NullSafeComparator<String> NULL_SAFE_STRING_COMPARATOR =
			new NullSafeComparator<String>(String.class);

	private static final LaunchGroupCategoryComparator LAUNCH_GROUP_CATEGORY_COMPARATOR =
			new LaunchGroupCategoryComparator();

	private static final LaunchGroupModeComparator LAUNCH_GROUP_MODE_COMPARATOR =
			new LaunchGroupModeComparator();

	private static final LaunchModeComparator LAUNCH_MODE_COMPARATOR =
			new LaunchModeComparator();

	public static NullSafeComparator<String> nullSafeStringComparator() {
		return NULL_SAFE_STRING_COMPARATOR;
	}

	public static LaunchGroupCategoryComparator launchGroupCategoryComparator() {
		return LAUNCH_GROUP_CATEGORY_COMPARATOR;
	}

	public static LaunchGroupModeComparator launchGroupModeComparator() {
		return LAUNCH_GROUP_MODE_COMPARATOR;
	}

	public static LaunchModeComparator launchModeComparator() {
		return LAUNCH_MODE_COMPARATOR;
	}

	private Comparators() {
	}

}
