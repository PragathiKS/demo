package com.tetralaval.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "FormConfiguration", description = "Tetra Laval form configuration")
public @interface FormConfiguration {

    @AttributeDefinition(name = "Parameters to be Ignored", description = "Parameters that are ignored that starts with these values E.g, :,_", type = AttributeType.STRING)
    String[] ignoreParameters();

    @AttributeDefinition(name = "Emails for various companies", description = "Email address mapping based on company, the email will be sent out to this contact", type = AttributeType.STRING)
    String[] emailMapping();

    @AttributeDefinition(name = "Action Types", description = "Action Types in the form dropdown. Example:- Mail:mail", type = AttributeType.STRING)
    String[] actionTypes();

    @AttributeDefinition(name = "Email Templates", description = "Email Templates in the form dropdown. Example:- Contact Us Mail Template:/etc/notification/email/tetralaval/contactus/email.html", type = AttributeType.STRING)
    String[] emailTemplates();
    
    @AttributeDefinition(name = "Root Path of the Contact Us Fragments", description = "Root Path of the content fragments where the contact us fragments are stored Example:- /content/dam/tetra-laval/content-fragments/contact-us", type = AttributeType.STRING)
    String contactUsFragmentsPath();

}
