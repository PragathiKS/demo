package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.LanguageBean;
import com.tetrapak.publicweb.core.beans.MarketBean;

import io.wcm.testing.mock.aem.junit.AemContext;

public class HeaderModelTest {
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/header/test-content.json";
    
    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT_TWO = "/header/test-global-content.json";

    /** The Constant MARKETS_CONTENT. */
    private static final String MARKETS_CONTENT = "/header/markets-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/language-masters/en";
    
    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT_TWO = "/content/tetrapak/publicweb/global";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content";
    
    /** The Constant RESOURCE. */
    private static final String RESOURCE_TWO = TEST_CONTENT_ROOT_TWO + "/en";

    /** The Constant MARKETS_CONTENT_ROOT. */
    private static final String MARKETS_CONTENT_ROOT = "/content/tetrapak/publicweb";
    
    /** The Constant RESOURCE_CONTENT1. */
    private static final String RESOURCE_CONTENT1 = "/megamenu/test-content.json";

    /** The Constant TEST_CONTENT_ROOT1. */
    private static final String TEST_CONTENT_ROOT1 = "/content/tetrapak/publicweb/language-masters/en";

    /** The Constant RESOURCE1. */
    private static final String RESOURCE1 = TEST_CONTENT_ROOT1 + "/jcr:content/root/responsivegrid/megamenuconfig";

    /** The model. */
    private HeaderModel model;

    /** The resource. */
    private Resource resource;
    
    /** The language bean. */
    LanguageBean languageBean = new LanguageBean();
    
    /** The market bean. */
    MarketBean marketBean = new MarketBean();

    @Mock
    private HeaderConfigurationModel headerConfig;
    
    @Mock
    private Page currentPage;
    
    Class<HeaderModel> modelClass = HeaderModel.class;
    
    /**
     * Sets the up.
     *
     * @param context
     *            the new up
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        
        MockSlingHttpServletRequest request = context.request();
        context.load().json(MARKETS_CONTENT, MARKETS_CONTENT_ROOT);
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        //context.load().json(RESOURCE_CONTENT1, TEST_CONTENT_ROOT1);
        context.addModelsForPackage("com.tetrapak.publicweb.core.models");

        context.request().setPathInfo(TEST_CONTENT_ROOT);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);
        
        marketBean.setMarketName("Belgium");
        marketBean.setCountryName("Belgique");
        languageBean.setLanguageName("French"); 
        languageBean.setCountryTitle("Belgique");
    }

    /**
     * Test model, resource and all getters of the ArticleContainer model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {             
        assertEquals("Header", "/content/tetrapak/publicweb/global/en.html", model.getLogoLink());
        assertEquals("Header", "Logo ", model.getLogoAlt());
        assertEquals("Header", "http://www.google.com", model.getLoginLink());
        assertEquals("Header", "Login Label", model.getLoginLabel());
        assertEquals("Header", "/content/dam/tetrapak/publicweb/global/header/header.png", model.getLogoImagePath());
        assertNotNull("Header", model.getMegaMenuConfigurationModel());
        assertEquals("Header", "/content/tetrapak/publicweb/language-masters/en/search.html", model.getSearchPage());
        assertEquals("Header", true, model.getMarketList().getMarkets().get(2).equals(marketBean));
        assertEquals("Header", "Belgium", model.getMarketList().getMarkets().get(2).getMarketName());
        assertEquals("Header", "Belgique", model.getMarketList().getMarkets().get(2).getCountryName());
        assertEquals("Header", "French",
                model.getMarketList().getMarkets().get(2).getLanguages().get(0).getLanguageName());
        assertEquals("Header", "Belgique",
                model.getMarketList().getMarkets().get(2).getLanguages().get(0).getCountryTitle());
        assertEquals("Header", "/content/tetrapak/publicweb/be/fr/home.html",
                model.getMarketList().getMarkets().get(2).getLanguages().get(0).getLinkPath());
        assertEquals("Header", true,
                model.getMarketList().getMarkets().get(2).getLanguages().get(0).equals(languageBean));
        assertEquals("Header", 3,
                model.getMarketList().getMarkets().get(2).getLanguages().get(0).getLanguageIndex());
        assertEquals("Header", "Choose Your Market", model.getMarketList().getMarketTitle());
        assertEquals("Header", "/content/tetrapak/publicweb/global/en/home.html",
                model.getMarketList().getGlobalMarketPath());
        assertEquals("Header", "", model.getCurrentMarket());
        assertEquals("Header", "English", model.getCurrentLanguage());
        assertEquals("Header", false, model.getDisplayCurrentLanguage());
        assertEquals("Header", 14, model.getMarketList().getCol1End());
        assertEquals("Header", 15, model.getMarketList().getCol2Start());
        assertEquals("Header", 28, model.getMarketList().getCol2End());
        assertEquals("Header", 29, model.getMarketList().getCol3Start());
        assertEquals("Header", 42, model.getMarketList().getCol3End());
        assertEquals("Header", 43, model.getMarketList().getCol4Start());
        assertEquals("Header", 54, model.getMarketList().getCol4End());
        assertEquals("Header", false, model.getMarketSelectorDisabled());
    }
    
    /**
     * Test end to end solution section.
     */
    @Test
    public void testEndToEndSolutionSection() {       
        
        assertEquals("Lorem ipsum", model.getEndToEndSolutionSection().get(0).getDescription());
        assertEquals("/content/dam/tetrapak/processing.jpg",
                model.getEndToEndSolutionSection().get(0).getFileReference());
        assertEquals("Processing", model.getEndToEndSolutionSection().get(0).getAlt());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en.html",
                model.getEndToEndSolutionSection().get(0).getPath());
        assertEquals("Processing", model.getEndToEndSolutionSection().get(0).getTitle());
    }

    /**
     * Test link section.
     */
    @Test
    public void testLinkSection() {        
        assertEquals("Aseptic solutions", model.getLinkSection().get(0).getLinkText());
        assertEquals("/content/tetrapak/publicweb/language-masters/en/aseptic.html",
                model.getLinkSection().get(0).getLinkUrl());
    }

    /**
     * Test food category section.
     */
    @Test
    public void testFoodCategorySection() {        
        assertEquals("/content/dam/tetrapak/cheese.jpg", model.getFoodCategorySection().get(0).getFileReference());
        assertEquals("Cheese", model.getFoodCategorySection().get(0).getAlt());
        assertEquals("/content/tetrapak/publicweb/lang-masters/en/food-categories/cheese.html",
                model.getFoodCategorySection().get(0).getPath());
        assertEquals("Cheese", model.getFoodCategorySection().get(0).getTitle());
    }
    
    @Test
    public void testGetMarketList() {
	AemContext contextMarket = new AemContext();
        MockSlingHttpServletRequest request = contextMarket.request();
        contextMarket.load().json(RESOURCE_CONTENT_TWO, TEST_CONTENT_ROOT_TWO); 
        contextMarket.request().setPathInfo(RESOURCE_TWO);
        request.setResource(contextMarket.resourceResolver().getResource(RESOURCE_TWO));
        resource = contextMarket.currentResource(RESOURCE_TWO);
        contextMarket.addModelsForPackage("com.tetrapak.publicweb.core.models");
        HeaderModel modalMarket = request.adaptTo(modelClass);
        
        assertEquals("Header", "Tetra Pak Global",
        	modalMarket.getMarketList().getGlobalMarketTitle());
        assertEquals("Header", "Tetra Pak Global",
        	modalMarket.getCountryTitle());
    }

}