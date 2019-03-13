package com.tetrapak.customerhub.core.mock;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import com.drew.lang.annotations.Nullable;
import com.google.common.base.Function;
import io.wcm.testing.mock.aem.junit.AemContext;
import io.wcm.testing.mock.aem.junit.AemContextCallback;

public class CuhuCoreAemContext {
    
    private CuhuCoreAemContext() {
        
    }
    
    public static AemContext getAemContext(String resourceJosnFileName, String resourcePath) {
        return new AemContext(new SetUpCallback<Object>(resourceJosnFileName, resourcePath),
                ResourceResolverType.RESOURCERESOLVER_MOCK);
    }
    
    public static <T> AemContext getAemContext(String resourceJosnFileName, String resourcePath,
            GenericServiceType<T> serviceType) {
        return new AemContext(new SetUpCallback<T>(resourceJosnFileName, resourcePath, serviceType),
                ResourceResolverType.RESOURCERESOLVER_MOCK);
    }
    
    public static <T> AemContext getAemContext(String resourceJosnFileName, String resourcePath,
            List<GenericServiceType<T>> serviceTypes) {
        return new AemContext(new SetUpCallback<T>(resourceJosnFileName, resourcePath, serviceTypes),
                ResourceResolverType.RESOURCERESOLVER_MOCK);
    }
    
    private static final class SetUpCallback<T> implements AemContextCallback {
        
        private String resourceJosnFileName = null;
        private String resourcePath = null;
        GenericServiceType<T> serviceType = null;
        List<GenericServiceType<T>> serviceTypes = null;
        
        SetUpCallback(String resourceJosnFileName, String resourcePath) {
            this.resourceJosnFileName = resourceJosnFileName;
            this.resourcePath = resourcePath;
        }
        
        SetUpCallback(String resourceJosnFileName, String resourcePath, GenericServiceType<T> serviceType) {
            this.resourceJosnFileName = resourceJosnFileName;
            this.resourcePath = resourcePath;
            this.serviceType = serviceType;
        }
        
        SetUpCallback(String resourceJosnFileName, String resourcePath, List<GenericServiceType<T>> serviceType) {
            this.resourceJosnFileName = resourceJosnFileName;
            this.resourcePath = resourcePath;
            this.serviceTypes = serviceType;
        }
        
        @Override
        public void execute(AemContext context) throws IOException {
            // Register Adapter
            context.registerAdapter(ResourceResolver.class, UserManager.class,
                    new Function<ResourceResolver, UserManager>() {
                        @Nullable
                        @Override
                        public UserManager apply(@Nullable ResourceResolver resolver) {
                            return new MockUserManager();
                        }
                    });
            //Register Single Service
            if (null != serviceType) {
                context.registerService(serviceType.getClazzType(), serviceType.get());
            }
            
            // Register multiple  Service
            if (null != serviceTypes && !serviceTypes.isEmpty()) {
                serviceTypes.forEach(service -> context.registerService(service.getClazzType(), service.get()));
            }
            
            // Register Sling Models
            context.addModelsForPackage("com.tetrapak.customerhub.core.models");
            if (StringUtils.isNotBlank(resourceJosnFileName) && StringUtils.isNotBlank(resourcePath)) {
                context.load().json("/" + resourceJosnFileName, resourcePath);
            }
        }
    }
}
