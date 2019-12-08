package com.tetrapak.customerhub.core.exceptions;

/**
 * Wrapper class for all the runtime exceptions while using the Azure Table storage service
 *
 * @author swalamba
 */
public class AzureRuntimeException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2361935275311441785L;

    /**
     * AzureRuntimeException constructor
     *
     * @param message value is be used.
     */
    public AzureRuntimeException(final String message) {
        super(message);

    }

    /**
     * AzureRuntimeException constructor
     *
     * @param message value is be used.
     * @param cause   throwable cause.
     */
    public AzureRuntimeException(final String message, final Throwable cause) {
        super(message, cause);

    }
}
