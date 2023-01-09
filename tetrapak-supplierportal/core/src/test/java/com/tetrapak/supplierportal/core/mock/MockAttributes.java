package com.tetrapak.supplierportal.core.mock;

import org.xml.sax.Attributes;

public class MockAttributes implements Attributes{
    
    private String URI;
    
    public MockAttributes(String URI) {
        this.URI = URI;
    }
    
    @Override
    public String getValue(String uri, String localName) {
        // TODO Auto-generated method stub
        return URI;
    }
    
    @Override
    public String getValue(String qName) {
        // TODO Auto-generated method stub
        return "href";
    }
    
    @Override
    public String getValue(int index) {
        // TODO Auto-generated method stub
        return URI;
    }
    
    @Override
    public String getURI(int index) {
        // TODO Auto-generated method stub
        return URI;
    }
    
    @Override
    public String getType(String uri, String localName) {
        // TODO Auto-generated method stub
        return "CDATA";
    }
    
    @Override
    public String getType(String qName) {
        // TODO Auto-generated method stub
        return "CDATA";
    }
    
    @Override
    public String getType(int index) {
        // TODO Auto-generated method stub
        return "CDATA";
     }
    
    @Override
    public String getQName(int index) {
        // TODO Auto-generated method stub
        return "href";
    }
    
    @Override
    public String getLocalName(int index) {
        // TODO Auto-generated method stub
        return "href";
    }
    
    @Override
    public int getLength() {
        // TODO Auto-generated method stub
        return 1;
    }
    
    @Override
    public int getIndex(String uri, String localName) {
        // TODO Auto-generated method stub
        return 1;
    }
    
    @Override
    public int getIndex(String qName) {
        // TODO Auto-generated method stub
        return 1;
    }

}
