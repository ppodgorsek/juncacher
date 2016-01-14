package com.github.ppodgorsek.juncacher.exception;

import org.junit.Test;

/**
 * Tests for the {@link InvalidationException} class.
 *
 * @author Paul Podgorsek
 */
public class InvalidationExceptionTest {

	@Test
	public void constructWithNoParams() {
		new InvalidationException();
	}

	@Test
	public void constructWithCorrectMessage() {
		new InvalidationException("This is a test");
	}

	@Test
	public void constructWithNullMessage() {
		new InvalidationException((String) null);
	}

	@Test
	public void constructWithCorrectCause() {
		new InvalidationException(new Exception());
	}

	@Test
	public void constructWithNullCause() {
		new InvalidationException((Throwable) null);
	}

	@Test
	public void constructWithCorrectMessageAndCorrectCause() {
		new InvalidationException("This is a test", new Exception());
	}

	@Test
	public void constructWithCorrectMessageAndNullCause() {
		new InvalidationException("This is a test", null);
	}

	@Test
	public void constructWithNullMessageAndCorrectCause() {
		new InvalidationException(null, new Exception());
	}

	@Test
	public void constructWithNullMessageAndNullCause() {
		new InvalidationException(null, null);
	}

}
