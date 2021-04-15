package com.trs.core.servlets;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.TagImporterServiceImpl;
import com.trs.core.utils.TestUtils;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class TagImporterTest {

    @Mock
    TrsConfigurationService trsConfig;

    @Mock
    private ResourceResolverFactory resolverFactory;

    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private TagImporter tagImporter;

    @BeforeEach
    void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        // setting content
        context.load().json("/com/trs/core/services/impl/videoFolder.json", "/content/dam/training-services/test");
        TestUtils.setupTrsConfiguration(context, trsConfig);
        Map<String, Object> properties = new HashMap<>();
        context.registerInjectActivateService(new TagImporterServiceImpl(), properties);
        Map<String, Object> props = new HashMap<>();
        props.put("sling.servlet.paths", "/bin/tagimporter");
        tagImporter = context.registerInjectActivateService(new TagImporter(), props);
    }

    @Test
    final void testDoPostSlingHttpServletRequestSlingHttpServletResponse()
            throws IOException, URISyntaxException, ServletException {
        MockSlingHttpServletResponse response = context.response();
        MockSlingHttpServletRequest request = context.request();

        request.addRequestParameter("file",
                Files.readAllBytes((new File(TagRenameUtilityServletTest.class
                        .getResource("/com/trs/core/services/impl/TrSTaxonomyAdobeDAM.xlsx").toURI())).toPath()),
                "binary/data");

        // Load resource being uploaded
        byte[] fileContent = Files.readAllBytes((new File(TagRenameUtilityServletTest.class
                .getResource("/com/trs/core/services/impl/TrSTaxonomyAdobeDAM.xlsx").toURI())).toPath());
        Part[] parts = new Part[] { new FilePart("file",
                new ByteArrayPartSource("/com/trs/core/services/impl/TrSTaxonomyAdobeDAM.xlsx", fileContent)) };
        MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(parts, new PostMethod().getParams());
        ByteArrayOutputStream requestContent = new ByteArrayOutputStream();
        multipartRequestEntity.writeRequest(requestContent);
        request.setContent(requestContent.toByteArray());
        request.setContentType(multipartRequestEntity.getContentType());
        request.setMethod("POST");
        tagImporter.doPost(request, response);
        assertNotNull(context.resourceResolver().getResource(trsConfig.getTrsXylemeMappingsPath()),
                "Resource should not be null");
    }

}
