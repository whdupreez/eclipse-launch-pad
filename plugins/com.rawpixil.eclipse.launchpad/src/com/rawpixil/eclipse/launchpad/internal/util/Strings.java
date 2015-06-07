package com.rawpixil.eclipse.launchpad.internal.util;

public final class Strings {

	public static boolean isNullOrWhitespace(String string) {
		return string == null || string.trim().isEmpty();
	}

	private Strings() {
	}

}
