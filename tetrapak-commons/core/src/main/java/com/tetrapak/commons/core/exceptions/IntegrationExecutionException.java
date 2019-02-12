package com.tetrapak.commons.core.exceptions;

/**
 * The Class IntegrationExecutionException.
 */
public class IntegrationExecutionException extends RuntimeException {
    
	/**
     * The Constant serialVersionUID.
     */
	private static final long serialVersionUID = -4800841163007914915L;

    /**
     * Instantiates a new integration execution exception.
     * @param message the message
     */
    public IntegrationExecutionException(final String message) {
        super(message);
        
    }

    /**
     * Instantiates a new integration execution exception.
     * @param message the message
     * @param cause the cause
     */
    public IntegrationExecutionException(final String message, final Throwable cause) {
        super(message, cause);
        
    }
    
}
