package com.tetrapak.commons.core.services;

/**
 * Dispatcher Flush Service
 *
 * @author Nitin Kumar
 */
@FunctionalInterface
public interface DispatcherFlushService {
    /**
     * @param dispatcherHandle path to flush cache
     */
    void flush(String dispatcherHandle);
}
