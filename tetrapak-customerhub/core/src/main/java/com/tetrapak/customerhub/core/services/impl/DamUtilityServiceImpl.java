package com.tetrapak.customerhub.core.services.impl;

import java.text.DecimalFormat;
import java.util.Iterator;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.services.DamUtilityService;
import com.tetrapak.customerhub.core.services.config.DamUtilityServiceConfig;

/**
 * Impl class for Dam Utility Service.
 *
 * @author Aalekh Mathur
 */
@Component(immediate = true, service = DamUtilityService.class)
@Designate(ocd = DamUtilityServiceConfig.class)
public class DamUtilityServiceImpl implements DamUtilityService {

    /** The config. */
    private DamUtilityServiceConfig config;

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DamUtilityServiceImpl.class);
    
    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");
    
    /**
     * activate method.
     *
     * @param config
     *            Dam Utility Service configuration
     */
    @Activate
    public void activate(final DamUtilityServiceConfig config) {
        this.config = config;
    }

    /**
     * return pdf assets from dam.
     *
     * @return the assets
     * @throws RepositoryException 
     * @throws PathNotFoundException 
     * @throws ValueFormatException 
     * @throws JsonProcessingException 
     * @throws JSONException 
     */
    @Override
    public String getAssetsFromDam(ResourceResolver resolver) throws JsonProcessingException, ValueFormatException, RepositoryException, JSONException {
    	Resource resource = resolver.getResource(config.DamUtilityRootPath());
    	return getData(resource).toString();
    }
    
    public static JSONObject getData(Resource resource) throws ValueFormatException, PathNotFoundException, RepositoryException, JSONException {
    	JSONObject jsonObject = new JSONObject();
    	Node node = resource.adaptTo(Node.class);
        if(node.getProperty(CustomerHubConstants.JCR_PRIMARY_TYPE).getString().toLowerCase().contains(CustomerHubConstants.CONSTANT_FOLDER)){
        	jsonObject.put(node.getName(), getChildren(resource));
        }else {
        	Asset asset = DamUtil.getAssets(resource).next();
        	float l = ((Long)asset.getMetadata().get(CustomerHubConstants.DAM_SIZE_PROPERTY)).floatValue()/(1024*1024);
        	Object[] obj = (Object[]) asset.getMetadata().get(CustomerHubConstants.DC_TITLE_PROPERTY);
        	jsonObject.put(CustomerHubConstants.ASSET_PATH, node.getPath());
        	jsonObject.put(CustomerHubConstants.ASSET_SIZE, decimalFormat.format(l) + CustomerHubConstants.SPACE + CustomerHubConstants.SIZE_MB);
        	jsonObject.put(CustomerHubConstants.ASSET_TITLE, asset.getMetadata().get(CustomerHubConstants.DC_TITLE_PROPERTY) != null ? obj[0] : "");
        }
        LOGGER.info("In DamUtilityServiceImpl, response is "+jsonObject.toString());
        return jsonObject;
    }
    
    public static JSONArray getChildren(Resource resource) throws ValueFormatException, PathNotFoundException, RepositoryException, JSONException{
    	JSONArray jsonArray = new JSONArray();
    	Iterator<Resource> itr = resource.listChildren();
    	while(itr.hasNext()) {
    		Resource res = itr.next();
			Node node = res.adaptTo(Node.class);
			String primaryType = node.getProperty(CustomerHubConstants.JCR_PRIMARY_TYPE).getString();
			Asset asset = DamUtil.getAssets(res).next();
			if((primaryType.equals(CustomerHubConstants.DAM_ASSETS_PROPERTY) && asset.getMetadata().get(CustomerHubConstants.DC_FORMAT_PROPERTY).toString().equalsIgnoreCase(CustomerHubConstants.APPLICATION_PDF))
					|| primaryType.toLowerCase().contains(CustomerHubConstants.CONSTANT_FOLDER)) {
				jsonArray.put(getData(res));
			}
    	}
    	return jsonArray;
    }    
}
