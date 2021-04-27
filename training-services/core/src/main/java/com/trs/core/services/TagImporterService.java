package com.trs.core.services;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.poi.ss.usermodel.Row;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;

import com.fasterxml.jackson.databind.ObjectMapper;


public interface TagImporterService {
    
    void iterateRows(ResourceResolver resourceResolver, Map<String, String> tagMappings,
            StringJoiner errorItems, final Iterator<Row> rows) throws PersistenceException;
    
    Map<String, String> readTagMappingFile(ResourceResolver resourceResolver, Map<String, String> tagMappings,ObjectMapper mapper)
            throws IOException;

}
