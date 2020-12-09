package com.tetrapak.publicweb.core.mock;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.engine.SlingRequestProcessor;

public class MockRequestProcessorImpl implements SlingRequestProcessor {

    @Override
    public void processRequest(HttpServletRequest arg0, HttpServletResponse arg1, ResourceResolver arg2)
            throws ServletException, IOException {
        // TODO Auto-generated method stub

    }

}
