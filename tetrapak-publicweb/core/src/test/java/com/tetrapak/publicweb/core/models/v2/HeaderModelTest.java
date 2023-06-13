package com.tetrapak.publicweb.core.models.v2;

import com.day.cq.wcm.api.Page;
import com.tetrapak.publicweb.core.beans.LanguageBean;
import com.tetrapak.publicweb.core.beans.MarketBean;
import com.tetrapak.publicweb.core.models.v2.HeaderConfigurationModel;
import com.tetrapak.publicweb.core.models.v2.HeaderModel;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HeaderModelTest {
    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/headerv2/test-content.json";
    
    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT_TWO = "/headerv2/test-global-content.json";

    /** The Constant MARKETS_CONTENT. */
    private static final String MARKETS_CONTENT = "/headerv2/markets-content.json";

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
    private com.tetrapak.publicweb.core.models.v2.HeaderModel model;

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
        context.addModelsForPackage("com.tetrapak.publicweb.core.models");

        context.request().setPathInfo(TEST_CONTENT_ROOT);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(HeaderModel.class);
        
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
        assertEquals(2,model.getSecondaryNavigationLinks().size());
        assertEquals(4,model.getMainNavigationLinks().size());
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
        HeaderModel modalMarket = request.adaptTo(HeaderModel.class);
        
        assertEquals("Header", "Tetra Pak Global",
        	modalMarket.getMarketList().getGlobalMarketTitle());
        assertEquals("Header", "Tetra Pak Global",
        	modalMarket.getCountryTitle());
    }
    
    @Test
    public void testGetGlobalMarket() {
        MockSlingHttpServletRequest request = context.request();
        context.load().json(RESOURCE_CONTENT_TWO, TEST_CONTENT_ROOT_TWO); 
        context.request().setPathInfo(RESOURCE_TWO);
        request.setResource(context.resourceResolver().getResource(RESOURCE_TWO));
        resource = context.currentResource(RESOURCE_TWO);
        context.addModelsForPackage("com.tetrapak.publicweb.core.models");
        model = request.adaptTo(HeaderModel.class);
        
        marketBean.setMarketName("Tetra Pak Global");
        marketBean.setCountryName("Global");
       
        assertEquals("Header", "Tetra Pak Global",
                model.getMarketList().getGlobalMarket().getMarketName());
    }

}