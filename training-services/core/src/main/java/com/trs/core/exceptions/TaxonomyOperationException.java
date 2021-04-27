package com.trs.core.exceptions;

/**
 * @author ankpatha
 * 
 *         This exception is thrown to indicate an issue with the fetching of
 *         Xyleme to AEM Tag mapping
 *
 */
public class TaxonomyOperationException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -9141425435057482995L;

    public TaxonomyOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
