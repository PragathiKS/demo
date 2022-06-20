package com.tetrapak.customerhub.core.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagConstants;
import com.day.cq.tagging.TagManager;
import com.tetrapak.customerhub.core.beans.keylines.Asset;
import com.tetrapak.customerhub.core.beans.keylines.Keylines;
import com.tetrapak.customerhub.core.beans.keylines.Opening;
import com.tetrapak.customerhub.core.beans.keylines.Shape;
import com.tetrapak.customerhub.core.beans.keylines.Volume;
import com.tetrapak.customerhub.core.constants.CustomerHubConstants;
import com.tetrapak.customerhub.core.exceptions.KeylinesException;
import com.tetrapak.customerhub.core.services.KeylinesService;
import com.tetrapak.customerhub.core.services.config.KeylinesConfiguration;

/**
 * Implementaion of the Keylines Service class
 * 
 * @author selennys
 *
 */

@Component(service = KeylinesService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = KeylinesConfiguration.class)
public class KeylinesServiceImpl implements KeylinesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeylinesServiceImpl.class);

    private static final String SEARCH_CQTAGS = "jcr:content/metadata/cq:tags";

    private static final String KEYNAME_SEPERATOR = "_";

    private KeylinesConfiguration config;

    @Activate
    public void activate(final KeylinesConfiguration config) {
	this.config = config;
    }

    public Keylines getKeylines(ResourceResolver resourceResolver, String packageType, List<String> shapes)
	    throws KeylinesException {
	LOGGER.trace("Inside getKeylines Method");
	Keylines keylines = null;
	TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
	List<String> tagIDs = new ArrayList<>();
	List<Tag> tags = new ArrayList<>();
	if (StringUtils.isNotBlank(packageType) && null != shapes && !shapes.isEmpty()) {
	    String packageTypeTagID = validateTags(tagManager, packageType, shapes, tagIDs, tags);
	    keylines = new Keylines();
	    keylines.setShapes(getShapesBean(tags));
	    List<Asset> keylineAssets = new ArrayList<>();
	    SearchResult result = query(resourceResolver, tagIDs);
	    if (null != result) {
		List<Hit> hits = result.getHits();
		LOGGER.debug("Hit size: {}", hits.size());

		for (Hit hit : hits) {
		    try {
			keylineAssets.addAll(getKeylineAssets(resourceResolver, packageTypeTagID, hit));

		    } catch (RepositoryException e) {
			LOGGER.error("Error while fetching the query", e);
		    }
		}
	    }
	    keylines.setAssets(keylineAssets);
	} else {
	    LOGGER.error("Package Type/Shape cannot be empty");
	    throw new KeylinesException("Package Type/Shape cannot be empty");
	}
	LOGGER.trace("End getKeylines Method");
	return keylines;

    }

    private List<Asset> getKeylineAssets(ResourceResolver resourceResolver, String packageTypeTagID, Hit hit)
	    throws RepositoryException {
	LOGGER.trace("Inside getKeylineAssets Method");
	List<Asset> keylineAssets = new ArrayList<>();

	String path = hit.getPath();
	Resource resource = resourceResolver.getResource(path);
	TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
	if (null != resource) {

	    String metadataPath = path + CustomerHubConstants.DAM_METADATA_PATH;
	    Resource metadataResource = resourceResolver.getResource(metadataPath);
	    if (null != metadataResource) {
		ValueMap properties = metadataResource.adaptTo(ValueMap.class);
		String[] cqTags = properties.get(TagConstants.PN_TAGS, String[].class);
		if (null != cqTags) {
		    for (String tag : cqTags) {
			if (StringUtils.startsWith(tag, packageTypeTagID + CustomerHubConstants.PATH_SEPARATOR)) {
			    Asset keylineAsset = new Asset();
			    keylineAsset.setAssetname(resource.getName());
			    keylineAsset.setAssetpath(path);
			    Tag keylineTag = tagManager.resolve(tag);
			    List<String> keyname = getKeyname(keylineTag, packageTypeTagID, new ArrayList<>());
			    keylineAsset.setKeyname(StringUtils.join(keyname, KEYNAME_SEPERATOR));
			    keylineAssets.add(keylineAsset);

			}
		    }
		}
	    }
	}
	LOGGER.trace("End getKeylineAssets Method");
	return keylineAssets;
    }

    private List<String> getKeyname(Tag keylineTag, String packageTypeTagID, List<String> keyname) {
	if (null != keylineTag && !StringUtils.equals(keylineTag.getTagID(), packageTypeTagID)) {
	    keyname.add(keylineTag.getName());
	    getKeyname(keylineTag.getParent(), packageTypeTagID, keyname);
	}
	return keyname;
    }

    private String validateTags(TagManager tagManager, String packageType, List<String> shapes, List<String> tagIDs,
	    List<Tag> tags) {
	LOGGER.trace("Inside validateTags Method");
	String packageTypeTagID = "";
	Tag packageTypeTag = tagManager.resolve(packageType);

	if (null != packageTypeTag) {
	    packageTypeTagID = packageTypeTag.getTagID();
	    for (String shape : shapes) {
		Tag shapeTag = tagManager.resolve(shape);
		/*
		 * Check if the shapes tags are child tags of package type, if so, add to the
		 * list of tags to be used for querying the assets
		 */
		if (null != shapeTag && StringUtils.contains(shapeTag.getTagID(), packageTypeTagID)) {
		    LOGGER.debug("Adding tag {}", shapeTag);
		    tagIDs.add(shapeTag.getTagID());
		    tags.add(shapeTag);
		} else {
		    LOGGER.debug("Could not resolve Shape tag {}", shapeTag);
		}
	    }
	} else {
	    LOGGER.debug("Could not resolve Package Type tag {}", packageType);
	}
	LOGGER.debug("Package Type ID {}", packageTypeTag);
	LOGGER.trace("End validateTags Method");
	return packageTypeTagID;
    }

    private List<Shape> getShapesBean(List<Tag> tags) {
	LOGGER.trace("Inside getShapesBean Method");
	List<Shape> shapes = new ArrayList<>();
	for (Tag tag : tags) {
	    Shape shape = new Shape();
	    LOGGER.debug("Shape name {}", tag.getName());
	    shape.setName(tag.getName());
	    setOpeningsAndVolumes(shape, tag);
	    shapes.add(shape);
	}
	LOGGER.trace("End getShapesBean Method");
	return shapes;
    }

    private void setOpeningsAndVolumes(Shape shape, Tag tag) {
	LOGGER.trace("Inside setOpeningsAndVolumes Method");
	Set<Volume> volumes = new HashSet<>();
	Set<Opening> openings = new HashSet<>();
	Iterator<Tag> childTags = tag.listChildren();
	while (childTags.hasNext()) {
	    Volume volume = new Volume();
	    Tag childTag = childTags.next();
	    volume.setKey(childTag.getName());
	    LOGGER.debug("Volume name {}", childTag.getName());
	    volume.setValue(childTag.getTitle());
	    LOGGER.debug("Volume Title {}", childTag.getTitle());
	    volumes.add(volume);
	    /* As openings is outside, creating a list and adding it to shapes */
	    openings.addAll(getOpenings(childTag));

	}
	shape.setOpenings(openings);
	shape.setVolumes(volumes);
	LOGGER.trace("End setOpeningsAndVolumes Method");
    }

    private Set<Opening> getOpenings(Tag tag) {
	LOGGER.trace("Inside getOpenings Method");
	Set<Opening> openings = new HashSet<>();
	Iterator<Tag> childTags = tag.listChildren();
	while (childTags.hasNext()) {
	    Opening opening = new Opening();
	    Tag childTag = childTags.next();
	    String openingName = childTag.getName();
	    opening.setKey(openingName);
	    LOGGER.debug("Opening name {}", openingName);
	    String openingTitle = childTag.getTitle();
	    opening.setValue(openingTitle);
	    LOGGER.debug("Opening Title {}", openingTitle);
	    openings.add(opening);
	}
	LOGGER.trace("End getOpenings Method");
	return openings;
    }

    private SearchResult query(ResourceResolver resourceResolver, List<String> tags) {
	LOGGER.trace("Inside query Method");

	/* Query media box keyline assets based on tags */
	SearchResult result = null;
	Map<String, String> map = new HashMap<>();
	QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
	Session session = resourceResolver.adaptTo(Session.class);

	if (!tags.isEmpty()) {
	    String path = this.config.path();
	    map.put("path", path);
	    LOGGER.debug("path={}", path);
	    String type = this.config.type();
	    map.put("type", type);
	    LOGGER.debug("type={}", type);
	    map.put("1_group.p.or", "true");
	    for (int i = 0; i < tags.size(); i++) {
		String tagID = "1_group." + (i + 1) + "_tagid";
		map.put(tagID, tags.get(i));
		LOGGER.debug("{}={}", tagID, tags.get(i));
		String property = tagID + ".property";
		map.put(property, SEARCH_CQTAGS);
		LOGGER.debug("{}={}", property, SEARCH_CQTAGS);
	    }
	    map.put("p.limit", "-1");
	    LOGGER.info("Here is the query PredicateGroup : {} ", PredicateGroup.create(map));
	    Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);

	    result = query.getResult();
	    LOGGER.debug("Query Result: {}", result);
	}
	LOGGER.trace("End query Method");
	return result;
    }

}
