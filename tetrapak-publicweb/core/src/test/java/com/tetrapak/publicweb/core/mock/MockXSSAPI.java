package com.tetrapak.publicweb.core.mock;

import org.apache.sling.xss.XSSAPI;

public class MockXSSAPI implements XSSAPI {

    String inputJson;

    public MockXSSAPI(final String inputJson) {
        this.inputJson = inputJson;
    }

    @Override
    public String encodeForCSSString(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeForHTML(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeForHTMLAttr(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeForJSString(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeForXML(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeForXMLAttr(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String filterHTML(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValidCSSColor(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValidDimension(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Double getValidDouble(final String arg0, final double arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValidHref(final String arg0) {
        // TODO Auto-generated method stub
        return inputJson;
    }

    @Override
    public Integer getValidInteger(final String arg0, final int arg1) {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public String getValidJSON(final String arg0, final String arg1) {
        return inputJson;
    }

    @Override
    public String getValidJSToken(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long getValidLong(final String arg0, final long arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValidMultiLineComment(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValidStyleToken(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValidXML(final String arg0, final String arg1) {
        // TODO Auto-generated method stub
        return null;
    }

}
