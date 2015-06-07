package com.rawpixil.eclipse.launchpad.internal.util;

public final class Assert {

	/**
	 * Ensures that an object reference is not null. A {@link NullPointerException}
	 * is thrown if the reference is null, using the provided error message as
	 * the exception message.
	 *
	 * @param reference
	 *            an object reference
	 * @param errorMessage
	 *            the exception message to use if the check fails; will be
	 *            converted to a string using {@link String#valueOf(Object)}
	 * @return the non-null reference that was validated
	 * @throws NullPointerException
	 *             if {@code reference} is null
	 * @param <T>
	 *            the object to assert
	 */
	public static <T> T notNull(T reference, Object errorMessage) {
		if (reference == null) {
			throw new NullPointerException(String.valueOf(errorMessage));
		}
		return reference;
	}

	/**
	 * Ensures the truth of an expression involving one or more parameters to
	 * the calling method.
	 *
	 * @param expression
	 *            a boolean expression
	 * @param errorMessage
	 *            the exception message to use if the check fails; will be
	 *            converted to a string using {@link String#valueOf(Object)}
	 * @throws IllegalArgumentException
	 *             if {@code expression} is false
	 */
	public static void argument(boolean expression, Object errorMessage) {
		if (!expression) {
			throw new IllegalArgumentException(String.valueOf(errorMessage));
		}
	}

	/**
	 * Ensures the truth of an expression involving the state of an object.
	 *
	 * @param expression
	 *            a boolean expression
	 * @param errorMessage
	 *            the exception message to use if the check fails; will be
	 *            converted to a string using {@link String#valueOf(Object)}
	 * @throws IllegalStateException
	 *             if {@code expression} is false
	 */
	public static void state(boolean expression, Object errorMessage) {
		if (!expression) {
			throw new IllegalStateException(String.valueOf(errorMessage));
		}
	}

	private Assert() {
	}

}
