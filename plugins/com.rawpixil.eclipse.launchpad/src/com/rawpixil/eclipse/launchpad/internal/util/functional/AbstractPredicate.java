package com.rawpixil.eclipse.launchpad.internal.util.functional;

import com.rawpixil.eclipse.launchpad.core.IPredicate;
import com.rawpixil.eclipse.launchpad.internal.util.Assert;

public abstract class AbstractPredicate<T> implements IPredicate<T> {

	public IPredicate<T> and(final IPredicate<T> predicate) {
		Assert.notNull(predicate, "Predicate cannot be null");
		return new IPredicate<T>() {
			@Override
			public boolean test(T t) {
				return AbstractPredicate.this.test(t) && predicate.test(t);
			}
		};
	}

	public IPredicate<T> or(final IPredicate<T> predicate) {
		Assert.notNull(predicate, "Predicate cannot be null");
		return new IPredicate<T>() {
			@Override
			public boolean test(T t) {
				return AbstractPredicate.this.test(t) || predicate.test(t);
			}
		};
	}

	public IPredicate<T> negate() {
		return new IPredicate<T>() {
			@Override
			public boolean test(T t) {
				return !AbstractPredicate.this.test(t);
			}
		};
	}

}
