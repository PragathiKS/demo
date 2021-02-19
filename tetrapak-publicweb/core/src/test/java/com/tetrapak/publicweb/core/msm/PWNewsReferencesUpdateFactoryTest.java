package com.tetrapak.publicweb.core.msm;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.day.cq.wcm.api.WCMException;
import com.day.cq.wcm.msm.api.LiveAction;

import io.wcm.testing.mock.aem.junit.AemContext;

public class PWNewsReferencesUpdateFactoryTest {
    
    /** The context. */
    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    
    PWNewsReferencesUpdateFactory pwNewsReferencesUpdateFactory = new PWNewsReferencesUpdateFactory();
    
    LiveAction rolloutAction;
    
    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception
     */
    @Before
    public void setUp() throws Exception {
        context.load().json("/msm/source.json", "/content/tetrapak/publicweb/lang-masters/en/home");
        context.load().json("/msm/target.json", "/content/tetrapak/publicweb/gb/en/home");
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testExecute() throws WCMException {
        Resource source = context.currentResource("/content/tetrapak/publicweb/lang-masters/en/home/jcr:content/root/responsivegrid/richtext");
        Resource target = context.currentResource("/content/tetrapak/publicweb/gb/en/home/jcr:content/root/responsivegrid/richtext");
        rolloutAction = pwNewsReferencesUpdateFactory.createAction(context.currentResource("/content/tetrapak/publicweb/lang-masters/en/home/jcr:content/root/responsivegrid/richtext"));
        rolloutAction.execute(source, target, null, true, true);
        assertEquals("PWNewsReferencesUpdateFactory", "pwUpdateReferrences",
                rolloutAction.getName());  
    }

}
