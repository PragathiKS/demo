package com.trs.core.services;

import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.scene7.api.S7Config;
import com.day.cq.dam.scene7.api.S7ConfigResolver;
import com.day.cq.search.result.Hit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface AssetMetadataService {

    List<Hit> executeQuery(ResourceResolver resourceResolver, Map<String, String> predicate);

    ObjectNode getAssetMetadataJsonNode(ResourceResolver resourceResolver, ObjectMapper mapper, String assetPath);

    S7Config resolveScene7Configuration(Asset asset, ResourceResolver rr, S7ConfigResolver s7ConfigResolver);
}
