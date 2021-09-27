package com.tetralaval.services.impl;

import com.day.cq.commons.Externalizer;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.tetralaval.config.SitemapSchedulerConfiguration;
import com.tetralaval.constants.TLConstants;
import com.tetralaval.models.sitemap.Sitemap;
import com.tetralaval.models.sitemap.SitemapIndex;
import com.tetralaval.models.sitemap.Url;
import com.tetralaval.models.sitemap.UrlSet;
import com.tetralaval.services.SitemapSchedulerService;
import com.tetralaval.utils.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SitemapSchedulerServiceImpl
 */
@Component(immediate = true, service = SitemapSchedulerService.class, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = SitemapSchedulerConfiguration.class)
public class SitemapSchedulerServiceImpl implements SitemapSchedulerService {
    /** LOGGER constant */
    private static final Logger LOGGER = LoggerFactory.getLogger(SitemapSchedulerServiceImpl.class);

    /** SITEMAP_NAMESPACE constant */
    private static final String SITEMAP_NAMESPACE = "http://www.sitemaps.org/schemas/sitemap/0.9";
    /** SITEMAP_XML constant */
    private static final String SITEMAP_XML = "sitemap.xml";
    /** HIDE_IN_SITEMAP constant */
    private static final String HIDE_IN_SITEMAP = "hideInSitemap";

    /** PATH_PLACEHOLDER constant */
    private static final String PATH_PLACEHOLDER = "%s/%s";

    /** resolverFactory */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** externalizer */
    @Reference
    private Externalizer externalizer;

    /** config */
    private SitemapSchedulerConfiguration config;

    /** resourceResolver */
    private ResourceResolver resourceResolver;
    /** session */
    private Session session;

    /**
     * activate method
     * @param config
     */
    @Activate
    public void activate(SitemapSchedulerConfiguration config) {
        this.config = config;
    }

    /**
     * schedulerName getter
     * @return schedulerName
     */
    @Override
    public String getSchedulerName() {
        return config.schedulerName();
    }

    /**
     * enabled getter
     * @return enabled
     */
    @Override
    public Boolean isEnabled() {
        return config.enabled();
    }

    /**
     * cronExpression getter
     * @return cronExpression
     */
    @Override
    public String getCronExpression() {
        return config.cronExpression();
    }

    /**
     * Generate sitemap method
     */
    @Override
    public void generateSitemap() {
        resourceResolver = GlobalUtil.getResourceResolverFromSubService(resolverFactory);

        List<String> countryCodes = getListOfCountryCodes();
        if (countryCodes != null) {
            try {
                byte[] sitemapIndex = getSitemapIndex(countryCodes);
                Node sitemapIndexNode = getSitemapLocationNode(TLConstants.ROOT_PATH);
                session = sitemapIndexNode.getSession();

                createSitemap(sitemapIndexNode, sitemapIndex);

                for (String countryCode : countryCodes) {
                    String path = String.format(PATH_PLACEHOLDER, TLConstants.ROOT_PATH, getMarketPath(countryCode));
                    byte[] marketUrlSet = getMarketSitemap(path, countryCode);
                    Node marketNode = getSitemapLocationNode(path);
                    createSitemap(marketNode, marketUrlSet);
                }
            } catch (Exception e) {
                LOGGER.error("Session fetch error = {}", e.getMessage(), e);
            } finally {
                if (session != null) {
                    session.logout();
                }
            }
        }
    }

    /**
     * Parse object to xml - bytes array
     * @param jaxbContext
     * @param collection
     * @param <T>
     * @return
     */
    private <T> byte[] objToXml(JAXBContext jaxbContext, T collection) {
        try {
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            File file = new File(SITEMAP_XML);

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(collection, file);
            return Files.readAllBytes(Paths.get(file.getPath()));
        } catch (JAXBException | IOException e) {
            LOGGER.error("Error during marshaller = {}", e.getMessage(), e);
        }
        return new byte[0];
    }

    /**
     * Get countryCode based on languagePage and countryPage names
     * @param languagePage
     * @param countryPage
     * @return country code
     */
    private String getCountryCode(Page languagePage, Page countryPage) {
        return String.format("%s%s%s", languagePage.getName(), TLConstants.HYPHEN, countryPage.getName());
    }

    /**
     * Get marketPath
     * @param countryCode
     * @return market path
     */
    private String getMarketPath(String countryCode) {
        String[] parts = countryCode.split(TLConstants.HYPHEN);
        return String.format(PATH_PLACEHOLDER, parts[1], parts[0]);
    }

    /**
     * Get list of country codes
     * @return
     */
    private List<String> getListOfCountryCodes() {
        List<String> countryCodes = null;

        Resource rootResource = resourceResolver.resolve(TLConstants.ROOT_PATH);
        if (rootResource != null && rootResource.adaptTo(Page.class) != null) {
            countryCodes = new ArrayList<>();

            Page rootPage = rootResource.adaptTo(Page.class);
            Iterator<Page> rootChildren = rootPage.listChildren();

            while (rootChildren != null && rootChildren.hasNext()) {
                Page countryPage = rootChildren.next();
                if (!TLConstants.LANGUAGE_MASTERS_PATH.equals(countryPage.getPath())
                        && !isHideInSitemap(countryPage.getPath())) {
                    Iterator<Page> countryChildren = countryPage.listChildren();
                    while (countryChildren != null && countryChildren.hasNext()) {
                        Page languagePage = countryChildren.next();
                        if (!isHideInSitemap(languagePage.getPath())) {
                            countryCodes.add(getCountryCode(languagePage, countryPage));
                        }
                    }
                }
            }
        }
        return countryCodes;
    }

    /**
     * Get node where sitemap should be stored
     * @param path
     * @return
     */
    private Node getSitemapLocationNode(String path) {
        Resource resource = resourceResolver.resolve(path);
        if (resource != null && resource.adaptTo(Node.class) != null) {
            return resource.adaptTo(Node.class);
        }
        return null;
    }

    /**
     * Generate and save sitemap
     * @param node
     * @param bytes
     */
    private void createSitemap(Node node, byte[] bytes) {
        Node sitemapNode;
        Node jcrContentNode;
        try {
            if (!node.hasNode(SITEMAP_XML)) {
                sitemapNode = node.addNode(SITEMAP_XML, JcrConstants.NT_FILE);
                jcrContentNode = sitemapNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_UNSTRUCTURED);
                jcrContentNode.setProperty(JcrConstants.JCR_MIMETYPE, "application/xml");
                session.save();
            } else {
                sitemapNode = node.getNode(SITEMAP_XML);
            }

            if (bytes.length > 0) {
                ValueFactory factory = session.getValueFactory();
                InputStream is = new ByteArrayInputStream(bytes);
                Binary binary = factory.createBinary(is);
                Value value = factory.createValue(binary);
                jcrContentNode = sitemapNode.getNode(JcrConstants.JCR_CONTENT);
                jcrContentNode.setProperty(JcrConstants.JCR_DATA, value);
                jcrContentNode.setProperty(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());
                session.save();
            }
        } catch (RepositoryException re) {
            LOGGER.error("Error during the sitemap creation = {}", re.getMessage(), re);
        }
    }

    /**
     * Generate sitemapIndex
     * @param countryCodes
     * @return
     */
    private byte[] getSitemapIndex(List<String> countryCodes) {
        SitemapIndex sitemapIndex = new SitemapIndex();
        sitemapIndex.setXmlns(SITEMAP_NAMESPACE);

        List<Sitemap> sitemaps = new ArrayList<>();
        for (String countryCode : countryCodes) {
            String path = String.format(PATH_PLACEHOLDER, TLConstants.ROOT_PATH, getMarketPath(countryCode));

            if (!isHideInSitemap(path)) {
                Sitemap sitemap = new Sitemap();
                try {
                    sitemap.setLocation(String.format("%s%s/%s", externalizer.externalLink(resourceResolver,
                            TLConstants.SITE_NAME, StringUtils.EMPTY), countryCode, SITEMAP_XML));
                } catch (Exception e) {
                    sitemap.setLocation(String.format("%s/%s/%s", TLConstants.DEFAULT_EXTERNALIZER, countryCode, SITEMAP_XML));
                    LOGGER.error("getSitemapIndex: set default externalizer path defined in TLConstants in case of missing configuration",
                            e.getMessage(), e);
                }
                sitemaps.add(sitemap);
            }
        }
        sitemapIndex.setSitemaps(sitemaps);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SitemapIndex.class);
            return objToXml(jaxbContext, sitemapIndex);
        } catch (JAXBException jaxbException) {
            LOGGER.error("Error during the creation of jaxbContext instance for sitemapIndex = {}",
                    jaxbException.getMessage(), jaxbException);
        }
        return new byte[0];
    }

    /**
     * Generate marketSitemap
     * @param path
     * @param countryCode
     * @return
     */
    private byte[] getMarketSitemap(String path, String countryCode) {
        List<Url> urls = new ArrayList<>();

        UrlSet urlSet = new UrlSet();
        urlSet.setXmlns(SITEMAP_NAMESPACE);
        urls.addAll(getPageForSitemap(path));

        urlSet.setUrls(urls.stream().map((Url url) -> {
            String newPath = url.getLocation().replace(path, countryCode);
            try {
                url.setLocation(String.format("%s%s", externalizer.externalLink(resourceResolver,
                        TLConstants.SITE_NAME, StringUtils.EMPTY), newPath));
            } catch (Exception e) {
                url.setLocation(String.format(PATH_PLACEHOLDER, TLConstants.DEFAULT_EXTERNALIZER, newPath));
                LOGGER.error("getMarketSitemap: set default externalizer path defined in TLConstants in case of missing configuration",
                        e.getMessage(), e);
            }
            return url;
        }).collect(Collectors.toList()));

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(UrlSet.class);
            return objToXml(jaxbContext, urlSet);
        } catch (JAXBException jaxbException) {
            LOGGER.error("Error during the creation of jaxbContext instance for urlset = {}",
                    jaxbException.getMessage(), jaxbException);
        }
        return new byte[0];
    }

    /**
     * Get urls for sitemap
     * @param path
     * @return list of url
     */
    private List<Url> getPageForSitemap(String path) {
        List<Url> urls = new ArrayList<>();
        if (!isHideInSitemap(path)) {
            Resource resource = resourceResolver.resolve(path);
            if (resource != null && resource.adaptTo(Page.class) != null) {
                Page page = resource.adaptTo(Page.class);

                Date date = page.getLastModified().getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                Url url = new Url();
                url.setLocation(page.getPath());
                url.setLastModification(format.format(date));
                urls.add(url);

                Iterator<Page> iterator = page.listChildren();
                while (iterator != null && iterator.hasNext()) {
                    Page childPage = iterator.next();
                    urls.addAll(getPageForSitemap(childPage.getPath()));
                }
            }
        }
        return urls;
    }

    /**
     * Check if page should be visible in sitemap
     * @param path
     * @return
     */
    private boolean isHideInSitemap(String path) {
        Resource resource = resourceResolver.resolve(path);
        if (resource != null && resource.adaptTo(Node.class) != null) {
            Node node = resource.adaptTo(Node.class);
            try {
                return node.hasProperty(String.format(PATH_PLACEHOLDER, JcrConstants.JCR_CONTENT, HIDE_IN_SITEMAP));
            } catch (RepositoryException re) {
                LOGGER.error("isHideInSitemap error = {}", re.getMessage(), re);
            }
        }
        return false;
    }
}
