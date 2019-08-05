package com.tetrapak.customerhub.core.exceptions;

/**
 * Wrapper class for all the runtime exceptions while handling security threats.
 * 
 * @author tustusha
 *
 */
public class SecutiyRuntimeException extends RuntimeException {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 6446722969844754805L;

	/**
     * SecutiyRuntimeException constructor
     * @param message value is be used.
     */
    public SecutiyRuntimeException(final String message) {
        super(message);

    }

    /**
     * SecutiyRuntimeException constructor
     * @param message value is be used.
     * @param cause throwable cause.
     */
    public SecutiyRuntimeException(final String message, final Throwable cause) {
        super(message, cause);

    }

	
}