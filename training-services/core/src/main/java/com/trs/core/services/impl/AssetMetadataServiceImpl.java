package com.trs.core.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.scene7.api.S7Config;
import com.day.cq.dam.scene7.api.S7ConfigResolver;
import com.day.cq.dam.scene7.api.Scene7Service;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trs.core.services.AssetMetadataService;

@Component(immediate = true, service = AssetMetadataService.class, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AssetMetadataServiceImpl implements AssetMetadataService {

    public static final String DAMSCENE7ID = "dam:scene7ID";
    private static final Logger LOG = LoggerFactory.getLogger(AssetMetadataServiceImpl.class);

    private Map<String, String> dmPropertyNameToAPIKeyMapping = Stream
            .of(new String[][] { { "Video Frame Rate", "video_frame_rate" }, { "Video Bit Rate", "video_bit_rate" },
                    { "Audio Codec", "audio_codec" }, { "Video Codec", "video_codec" },
                    { "Video Length", "video_length" }, { "Audio Sample Rate", "audio_sample_rate" },
                    { "Video Width", "video_width" }, { "Number Audio Channels", "number_audio_channels" },
                    { "fileSize", "file_size" }, { "Total Stream Bit Rate", "total_stream_bit_rate" },
                    { "Audio Bit Rate", "audio_bit_rate" }, { "Video Height", "video_height" } })
            .collect(Collectors.toMap(data -> data[0], data -> data[1]));

    @Reference
    private QueryBuilder builder;

    @Reference
    private Scene7Service scene7Service;

    @Reference
    private S7ConfigResolver s7ConfigResolver;

    public List<Hit> executeQuery(ResourceResolver resourceResolver, Map<String, String> predicate) {

        Query query = builder.createQuery(PredicateGroup.create(predicate), resourceResolver.adaptTo(Session.class));

        query.setStart(0);

        SearchResult searchResult = query.getResult();

        return searchResult.getHits();
    }

    public ObjectNode getAssetMetadataJsonNode(ResourceResolver resourceResolver, ObjectNode assetNode, String assetPath) {
        
        LOG.info("assetPath" + assetPath);
        Asset asset = resourceResolver.getResource(assetPath).adaptTo(Asset.class);
        LOG.info(String.valueOf(asset != null));
        S7Config s7Config = resolveScene7Configuration(asset, resourceResolver, s7ConfigResolver);

        if (null != s7Config) {
            String assetHandle = asset.getMetadataValue(DAMSCENE7ID);

            if (null != scene7Service && null != assetHandle) {
                Map<String, String> assetMetadata = scene7Service.getS7AssetMetadata(s7Config, assetHandle);
                for (Map.Entry<String, String> entry : dmPropertyNameToAPIKeyMapping.entrySet()) {
                    assetNode.put(entry.getValue(), assetMetadata.get(entry.getKey()));
                }
            }
        }

        return assetNode;
    }

    public S7Config resolveScene7Configuration(Asset asset, ResourceResolver rr, S7ConfigResolver s7ConfigResolver) {
        Resource resource = asset.adaptTo(Resource.class);

        String s7ConfigPath = s7ConfigResolver.getS7ConfigPathForResource(rr, resource);
        S7Config config = s7ConfigResolver.getS7Config(rr, s7ConfigPath);
        if (config == null) {
            LOG.error("Could not resolve a S7Config from resource {}",
                    (resource != null ? resource.getPath() : "null"));
        }

        return config;
    }

}
