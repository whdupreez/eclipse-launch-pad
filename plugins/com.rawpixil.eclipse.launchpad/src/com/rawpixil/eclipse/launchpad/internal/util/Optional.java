package com.rawpixil.eclipse.launchpad.internal.util;

import java.util.NoSuchElementException;

public class Optional<T> {

	private static final Optional<?> EMPTY = new Optional<Object>();

	public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

    public static <T> Optional<T> empty() {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) EMPTY;
        return t;
    }

    public static <T> Optional<T> ofNullable(T value) {
    	if (value == null) {
    		return empty();
    	}
    	else {
    		return of(value);
    	}
    }

    private final T value;

	private Optional() {
		this.value = null;
	}

    private Optional(T value) {
        this.value = Assert.notNull(value, "Value cannot be null");
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    @Override
    public String toString() {
        return value != null
            ? String.format("Optional[%s]", value)
            : "Optional.empty";
    }

}
