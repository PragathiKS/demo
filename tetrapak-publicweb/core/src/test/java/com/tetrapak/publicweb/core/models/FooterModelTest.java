package com.tetrapak.publicweb.core.models;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import io.wcm.testing.mock.aem.junit.AemContext;

public class FooterModelTest {

    @Rule
    public AemContext context = new AemContext();

    /** The Constant RESOURCE_CONTENT. */
    private static final String RESOURCE_CONTENT = "/footer/test-content.json";

    /** The Constant TEST_CONTENT_ROOT. */
    private static final String TEST_CONTENT_ROOT = "/content/tetrapak/publicweb/language-masters/en";

    /** The Constant RESOURCE. */
    private static final String RESOURCE = TEST_CONTENT_ROOT + "/jcr:content";

    /** The model. */
    private FooterModel model;

    /** The resource. */
    private Resource resource;

    @Mock
    private FooterConfigurationModel footerConfig;

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

        Class<FooterModel> modelClass = FooterModel.class;
        MockSlingHttpServletRequest request = context.request();
        context.load().json(RESOURCE_CONTENT, TEST_CONTENT_ROOT);
        context.addModelsForClasses(modelClass);

        context.request().setPathInfo(TEST_CONTENT_ROOT);
        request.setResource(context.resourceResolver().getResource(RESOURCE));
        resource = context.currentResource(RESOURCE);
        model = request.adaptTo(modelClass);
    }

    /**
     * Test model, resource and all getters of the Footer model.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    public void simpleLoadAndGettersTest() throws Exception {
        assertEquals("/content/tetrapak/publicweb/lang-masters.html", model.getLogoLink());
        assertEquals("Logo ", model.getLogoAlt());
        assertEquals("Go To Top", model.getGoToTopLabel());
        assertEquals("Link text 2", model.getFooterLinks().get(1).getLinkLabel());
        assertEquals("/content/tetrapak/publicweb.html", model.getFooterLinks().get(1).getLinkPath());
        assertEquals("Linkedin", model.getSocialLinks().get(0).getSocialMedia());
        assertEquals("http://www.google.com", model.getSocialLinks().get(0).getSocialMediaLink());
        assertEquals("QR code Text",model.getWechatQrCodeText());
        assertEquals("/content/dam/tetrapak/publicweb/gb/TetraPak-Wechat-QR-code.png", model.getWechatQrCodeReference());
        assertEquals("Alt Text",model.getQrAltText());
        assertEquals("/content/dam/tetrapak/publicweb/gb/App-Store.png", model.getAppStoreReference());
        assertEquals("App store alt text", model.getAppStoreAltText());
        assertEquals("/content/dam/tetrapak/publicweb/gb/Google-Play.png",model.getGooglePlayReference());
        assertEquals("Google play alt text",model.getGooglePlayAltText());
    }
}
