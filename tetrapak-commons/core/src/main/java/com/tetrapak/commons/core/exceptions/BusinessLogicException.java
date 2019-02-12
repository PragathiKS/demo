package com.tetrapak.commons.core.exceptions;

/**
 * BusinessLogicException class for the exceptions
 */
public class BusinessLogicException extends Exception {
    
    /**
     * The Constant serialVersionUID.
     */
	private static final long serialVersionUID = -854094849797473815L;

	/**
     * BusinessLogicException constructor
     * @param message value is be used.
     */
    public BusinessLogicException(final String message) {
        super(message);
        
    }
    
    /**
     * BusinessLogicException constructor
     * @param message value is be used.
     * @param cause throwable cause.
     */
    public BusinessLogicException(final String message, final Throwable cause) {
        super(message, cause);
        
    }
    
}
