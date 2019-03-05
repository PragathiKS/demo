package com.tetrapak.customerhub.core.mock;

import java.io.IOException;

import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import com.drew.lang.annotations.Nullable;
import com.google.common.base.Function;
import com.tetrapak.customerhub.core.servlets.SimpleServlet;
import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class CuhuCoreAemContext {
	public static final String GET_STARTED_CONTENT_ROOT = "/content/tetrapak/customerhub/global/about-us/jcr:content/par/getstarted"; 
	public static final String GET_SIMPLE_SERVLET_ROOT = "/content/test"; 
	
	private CuhuCoreAemContext() {
		
	}
	
	public static AemContext getAemContext() {
		return new AemContext(new SetUpCallback(), ResourceResolverType.RESOURCERESOLVER_MOCK);
    }
	
	private static final class SetUpCallback implements AemContextCallback {

	        @Override
	        public void execute(AemContext context) throws IOException {
	            //Register Adapter 
	            context.registerAdapter(ResourceResolver.class, UserManager.class, new Function<ResourceResolver, UserManager>() {
	                @Nullable
	                @Override
	                public UserManager apply(@Nullable ResourceResolver resolver) {
	                    return new MockUserManager();
	                }
	            });
	            //Register Service
	            context.registerService(SimpleServlet.class, new SimpleServlet());
	            context.load().json("/simpleservlet.json", GET_SIMPLE_SERVLET_ROOT);
	            //Register Sling Models
	            context.addModelsForPackage("com.tetrapak.customerhub.core.models");
	            context.load().json("/getstarted.json", GET_STARTED_CONTENT_ROOT);
	        }
	    }
}
