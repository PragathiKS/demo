package com.tetrapak.publicweb.core.services.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * The Interface DataEncryptionServiceConfig.
 */
@ObjectClassDefinition(
        name = "Public Web AES Data Encryption config",
        description = "Public Web Create Live Copy Config")
public @interface DataEncryptionServiceConfig {

    /**
     * Aes encryptionkey.
     *
     * @return the string
     */
    @AttributeDefinition(
            name = "AES encryption key",
            description = "Publicweb AES encryption key",
            type = AttributeType.STRING)
    String aesEncryptionkey();

}
