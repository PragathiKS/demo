package com.tetrapak.commons.core.exceptions;

/**
 * This is IntegrationLogicException class.
 */
public class IntegrationLogicException extends Exception {
    
	/**
     * The Constant serialVersionUID.
     */
	private static final long serialVersionUID = 4113485699834298094L;

    /**
     * @param message
     */
    public IntegrationLogicException(final String message) {
        super(message);

    }

    /**
     * @param message
     * @param cause
     */
    public IntegrationLogicException(final String message, final Throwable cause) {
        super(message, cause);

    }

}
