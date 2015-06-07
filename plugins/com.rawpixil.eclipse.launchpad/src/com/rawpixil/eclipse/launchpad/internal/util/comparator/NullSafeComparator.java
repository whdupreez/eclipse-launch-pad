package com.rawpixil.eclipse.launchpad.internal.util.comparator;

import java.util.Comparator;

import com.rawpixil.eclipse.launchpad.internal.util.Assert;

/**
 * A null safe comparator useful when comparing fields, e.g
 * o1.field with o2.field.
 *
 * @author Willy du Preez
 *
 * @param <T> the compared type
 */
public class NullSafeComparator<T> implements Comparator<T> {

	private Comparator<T> comparator;

	public NullSafeComparator(Class<T> comparable) {
		Assert.argument(Comparable.class.isAssignableFrom(comparable), "Type must implement java.lang.Comparable<T>");
	}

	public NullSafeComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	public int compare(T o1, T o2) {
		if (o1 == o2) {
			return 0;
		}
		else if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		else if (o2 == null) {
			return Integer.MAX_VALUE;
		}
		else if (comparator == null) {
			// Type safety ensured by type constructor.
			@SuppressWarnings("unchecked")
			int compareTo = Comparable.class.cast(o1).compareTo(o2);
			return compareTo;
		}
		else {
			return this.comparator.compare(o1, o2);
		}
	}

}
