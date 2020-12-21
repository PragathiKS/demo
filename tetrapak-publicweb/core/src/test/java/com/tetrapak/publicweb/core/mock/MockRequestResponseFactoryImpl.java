package com.tetrapak.publicweb.core.mock;

import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;

public class MockRequestResponseFactoryImpl implements RequestResponseFactory {

    @Override
    public HttpServletRequest createRequest(String arg0, String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServletRequest createRequest(String arg0, String arg1, Map<String, Object> arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServletResponse createResponse(OutputStream arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMD5(HttpServletResponse arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
