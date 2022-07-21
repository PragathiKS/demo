package com.tetrapak.customerhub.core.exceptions;

/**
 * Wrapper class for all the runtime exceptions while using the Keylines service
 *
 * @author selennys
 */
public class KeylinesException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6286876068611602151L;

    /**
     * KeylinesException constructor
     *
     * @param message value is be used.
     */
    public KeylinesException(final String message) {
	super(message);

    }

    /**
     * KeylinesException constructor
     *
     * @param message value is be used.
     * @param cause   throwable cause.
     */
    public KeylinesException(final String message, final Throwable cause) {
	super(message, cause);

    }
}
