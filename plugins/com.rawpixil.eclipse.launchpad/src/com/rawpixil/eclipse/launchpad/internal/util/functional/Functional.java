package com.rawpixil.eclipse.launchpad.internal.util.functional;

import java.util.ArrayList;
import java.util.List;

import com.rawpixil.eclipse.launchpad.core.IFunction;
import com.rawpixil.eclipse.launchpad.core.IPredicate;

public final class Functional {

	public static <T> List<T> filter(List<T> items, IPredicate<? super T> predicate) {
		List<T> matched = new ArrayList<T>();
		for (T item : items) {
			if (predicate.test(item)) {
				matched.add(item);
			}
		}
		return matched;
	}

	public static <T, R> R map(IFunction<T, R> function, T item) {
		return function.apply(item);
	}

	public static <T, R> List<R> map(IFunction<T, R> function, List<T> list) {
		List<R> mapped = new ArrayList<R>();
		for (T item : list) {
			mapped.add(function.apply(item));
		}
		return mapped;
	}

	public static <T, R> List<R> map(IFunction<T, R> function, T[] array) {
		List<R> mapped = new ArrayList<R>();
		for (T item : array) {
			mapped.add(function.apply(item));
		}
		return mapped;
	}

	private Functional() {
	}

}
