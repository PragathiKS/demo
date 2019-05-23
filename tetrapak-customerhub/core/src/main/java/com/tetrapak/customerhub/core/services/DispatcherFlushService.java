package com.tetrapak.customerhub.core.services;

/**
 * Custom Dispatcher Flush Service for the Customer Hub pages incase of
 * activation of the pages to map and remove the country and locale specific
 * cache
 * 
 * @author Swati Lamba
 *
 */
public interface DispatcherFlushService {

	/**
	 * This method will flush the cache of Customer Hub pages incase of activation
	 * of the pages to map and remove the country and locale specific cache
	 * 
	 * @param dispatcherHandle
	 */
	public void flush(String dispatcherHandle);

}
