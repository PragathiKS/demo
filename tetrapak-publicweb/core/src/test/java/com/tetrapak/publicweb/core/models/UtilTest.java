package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.sling.api.resource.Resource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilTest {

	/** Logger Instantiation. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UtilTest.class);

	/**
	 * 
	 * @param methods     the methods
	 * @param modelObject the modelObject
	 * @param resource    the resource
	 */

	@Test
	public void simpleLoadAndGettersTest() {
		LOGGER.debug("inside test method");
	}

	public static void testLoadAndGetters(String methods[], Object modelObject, Resource resource) {

		assertNotNull("Checking if Object is not null", modelObject);
		assertNotNull("Checking if resource is not null", resource);

		for (String methodStr : methods) {
			try {
				Method method = modelObject.getClass().getMethod(methodStr);
				assertNotNull("Checking All Getters of the model class", method.invoke(modelObject));

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				LOGGER.error("Exception in the getters test method :: {}", e);
			}
		}
	}
}
