package org.ppodgorsek.cache.invalidation.exception;

/**
 * Exception thrown when an invalidation didn't perform correctly.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class InvalidationException extends Exception {

	private static final long serialVersionUID = 3873866889373653053L;

	/**
	 * Default constructor.
	 */
	public InvalidationException() {
		super();
	}

	/**
	 * Constructor allowing to set the exception's message.
	 *
	 * @param message
	 *            The message.
	 */
	public InvalidationException(final String message) {
		super(message);
	}

	/**
	 * Constructor allowing to set the exception's cause.
	 *
	 * @param cause
	 *            The cause.
	 */
	public InvalidationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor allowing to set the exception's message and cause.
	 *
	 * @param message
	 *            The message.
	 * @param cause
	 *            The cause.
	 */
	public InvalidationException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
