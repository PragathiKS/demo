package com.tetrapak.publicweb.core.models;

import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

public class Util {

    /**
     * Logger Instantiation.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    /**
     * @param methods     the methods
     * @param modelObject the modelObject
     * @param resource    the resource
     */

    public static void testLoadAndGetters(String methods[], Object modelObject, Resource resource) {

        assertNotNull("Checking if Object is not null", modelObject);
        assertNotNull("Checking if resource is not null", resource);

        for (String methodStr : methods) {
            try {
                Method method = modelObject.getClass().getMethod(methodStr);
                assertNotNull("Checking All Getters of the model class", method.invoke(modelObject));

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                LOGGER.error("Exception in the getters test method ::", e);
            }
        }
    }
}
