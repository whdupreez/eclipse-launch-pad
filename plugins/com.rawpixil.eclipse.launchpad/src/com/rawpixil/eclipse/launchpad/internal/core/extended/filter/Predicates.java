package com.rawpixil.eclipse.launchpad.internal.core.extended.filter;

public final class Predicates {

	private static final Favorites FAVORITES = new Favorites();

	public static Favorites favorites() {
		return FAVORITES;
	}

	private Predicates() {
	}

}
