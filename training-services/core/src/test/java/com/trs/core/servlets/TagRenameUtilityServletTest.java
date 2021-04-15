package com.trs.core.servlets;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.day.cq.replication.Replicator;
import com.trs.core.services.TrsConfigurationService;
import com.trs.core.services.impl.TaxonomyServiceImpl;
import com.trs.core.utils.TestUtils;
import com.trs.core.utils.TrsConstants;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
public class TagRenameUtilityServletTest {

    @Mock
    TrsConfigurationService trsConfig;

    @Mock
    private ResourceResolverFactory resolverFactory;

    @Mock
    private Replicator replicator;

    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private TagRenameUtilityServlet tagRenameUtilityServlet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        // setting content
        context.load().json("/com/trs/core/services/impl/videoFolder.json", "/content/dam/training-services/test");
        // registering configuration service
        TestUtils.setupTrsConfiguration(context, trsConfig);
        context.registerService(TrsConfigurationService.class, trsConfig);
        Mockito.when(trsConfig.getXylemeTagsPropertyName()).thenReturn("dam:xylemeTags");
        // registering services
        context.registerService(Replicator.class, replicator);
        TestUtils.setUpTaxonomyService(context);
        Map<String, Object> props = new HashMap<>();
        context.registerInjectActivateService(new TaxonomyServiceImpl(), props);
        // registering servlet
        props.put("sling.servlet.paths", "/bin/tagrenameutility");
        tagRenameUtilityServlet = context.registerInjectActivateService(new TagRenameUtilityServlet(), props);

    }

    @Test
    final void testDoPostSlingHttpServletRequestSlingHttpServletResponse()
            throws IOException, URISyntaxException, ServletException {

        MockSlingHttpServletResponse response = context.response();
        MockSlingHttpServletRequest request = context.request();

        request.addRequestParameter("file",
                Files.readAllBytes((new File(TagRenameUtilityServletTest.class
                        .getResource("/com/trs/core/services/impl/trs-metadata.csv").toURI())).toPath()),
                "binary/data");

        // Load resource being uploaded
        byte[] fileContent = Files.readAllBytes((new File(
                TagRenameUtilityServletTest.class.getResource("/com/trs/core/services/impl/trs-metadata.csv").toURI()))
                        .toPath());
        // Create part & entity from resource
        Part[] parts = new Part[] { new FilePart("file",
                new ByteArrayPartSource("/com/trs/core/services/impl/trs-metadata.csv", fileContent)) };
        MultipartRequestEntity multipartRequestEntity = new MultipartRequestEntity(parts, new PostMethod().getParams());
        // Serialize request body
        ByteArrayOutputStream requestContent = new ByteArrayOutputStream();
        multipartRequestEntity.writeRequest(requestContent);
        request.setContent(requestContent.toByteArray());
        request.setContentType(multipartRequestEntity.getContentType());
        request.setMethod("POST");
        tagRenameUtilityServlet.doPost(request, response);
        String[] cqTagsArray = new String[] {
                "trs:ts_level_category/general/tetra_pak_packaging/filling_machine/tetra_pak_a3/tetra_pak_a3_cf/tetra_pak_a3_cf_0200",
                "trs:ts_level_category/general/tetra_pak_packaging/filling_machine/tetra_pak_a3/tetra_pak_a3_cf/tetra_pak_a3_cf_0400" };
        Assertions.assertArrayEquals(cqTagsArray,
                context.resourceResolver().getResource(TestUtils.TEST_ASSET_PATH)
                        .getChild(TrsConstants.METADATA_NODE_RELATIVE_PATH).getValueMap()
                        .get(TrsConstants.CQ_TAGS_PROPERTY, String[].class),
                "Unexpected value of cq:tags");

    }

}
