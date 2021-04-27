package com.trs.core.exceptions;

/**
 * @author ankpatha
 * 
 *         This exception is thrown to indicate an issue with the creation of
 *         AEM Page
 *
 */
public class PageNotCreatedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7732470905037545763L;

    public PageNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

}
