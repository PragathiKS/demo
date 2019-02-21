/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tetrapak.customerhub.core.models;

import com.tetrapak.customerhub.core.beans.GetStartedBean;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GetStartedModelTest {

    //@Inject
    private GetStartedModel getStartedModel;

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void setup() {
        context.addModelsForPackage("com.tetrapak.customerhub.core.models");
        context.build().resource("/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted",
                "sling:resourceType", "customerhub/components/content/getstarted",
                "headingI18n", "About_GS_Heading"
        ).commit();
        context.build().resource("/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted/list",
                "jcr:primaryType", "nt:unstructured"
        ).commit();
        context.build().resource("/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted/list/item0",
                "jcr:primaryType", "nt:unstructured",
                "titleI18n", "About_GS_title1",
                "descriptionI18n", "About_GS_Desc1",
                "imageAltI18n", "About_GS_Image1_alt",
                "imagePath", "/content/dam/customerhub/p2.PNG"
        ).commit();
        Resource currentResource = context.currentResource("/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted");
        getStartedModel = currentResource.adaptTo(GetStartedModel.class);
    }

    @Test
    public void testGetMessage() throws Exception {
        String heading = getStartedModel.getHeadingI18n();
        assertNotNull(heading);
        assertTrue(heading.length() > 0);
        Assert.assertEquals("About_GS_Heading",getStartedModel.getHeadingI18n());
        assertTrue(getStartedModel.getGetStartedList().size() > 0);
        List list = getStartedModel.getGetStartedList();
        GetStartedBean bean = (GetStartedBean) list.get(0);
        Assert.assertEquals("About_GS_title1",bean.getTitleI18n());
        Assert.assertEquals("About_GS_Desc1",bean.getDescriptionI18n());
        Assert.assertEquals("About_GS_Image1_alt",bean.getImageAltI18n());
        Assert.assertEquals("/content/dam/customerhub/p2.PNG",bean.getImagePath());
    }

}
