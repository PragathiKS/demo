package com.tetrapak.customerhub.core.services.impl;

import com.tetrapak.customerhub.core.services.EquipmentDetailsService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Class EquipmentDetailsServiceImpl.
 */
@Component(service = EquipmentDetailsService.class, immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Designate(ocd = EquipmentDetailsServiceImpl.Config.class)
public class EquipmentDetailsServiceImpl implements EquipmentDetailsService {

    /**
     * The Interface Config.
     */
    @ObjectClassDefinition(
            name = "Tetra Pak - Equipment Details Service",
            description = "Tetra Pak - Equipment Details Service")
    public static @interface Config {

    }

    @Override
    public HttpStatus addEquipment(SlingHttpServletRequest request) {
        return null;
    }

    @Override
    public HttpStatus editEquipment(SlingHttpServletRequest request) {
        return null;
    }
}
