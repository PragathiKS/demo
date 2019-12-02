package com.tetrapak.customerhub.core.exceptions;

/**
 * Wrapper class for all the runtime exceptions while using generate excel report
 *
 * @author dhitiwar
 */
public class ExcelReportRuntimeException extends RuntimeException {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2361935275311441785L;

    /**
     * ExcelReportRuntimeException constructor
     *
     * @param message value is be used.
     */
    public ExcelReportRuntimeException(final String message) {
        super(message);

    }

    /**
     * ExcelReportRuntimeException constructor
     *
     * @param message value is be used.
     * @param cause   throwable cause.
     */
    public ExcelReportRuntimeException(final String message, final Throwable cause) {
        super(message, cause);

    }
}
