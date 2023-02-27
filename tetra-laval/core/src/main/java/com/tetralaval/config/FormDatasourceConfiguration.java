package com.tetralaval.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "TetraLaval: Form Datasource Configuration", description = "Datasource configuration for Tetra Laval Forms")
public @interface FormDatasourceConfiguration {

    @AttributeDefinition(name = "Action Types", description = "Action Types in the form dropdown. Example:- Mail:mail", type = AttributeType.STRING)
    String[] actionTypes();
    
    @AttributeDefinition(name = "Email Templates", description = "Email Templates in the form dropdown. Example:- Contact Us Mail Template:/etc/notification/email/tetralaval/contactus/email.html", type = AttributeType.STRING)
    String[] emailTemplates();

}
