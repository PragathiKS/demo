package com.tetrapak.publicweb.core.services.impl;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tetrapak.publicweb.core.constants.PWConstants;
import com.tetrapak.publicweb.core.services.DataEncryptionService;
import com.tetrapak.publicweb.core.services.config.DataEncryptionServiceConfig;

/**
 * The Class DataEncryptionServiceImpl.
 */
@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, service = DataEncryptionService.class)
@Designate(ocd = DataEncryptionServiceConfig.class)
public class DataEncryptionServiceImpl implements DataEncryptionService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataEncryptionServiceImpl.class);

    /** The config. */
    private DataEncryptionServiceConfig config;

    /** The secret key. */
    private SecretKeySpec secretKey;

    /**
     * Activate.
     *
     * @param config
     *            the config
     */
    @Activate
    public void activate(final DataEncryptionServiceConfig config) {
        this.config = config;
        String encryptionKey = getDataEncryptionKey();
        byte[] key;
        MessageDigest sha = null;
        try {
            key = encryptionKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Error while DataEncryptionService activation {}", e.getMessage());
        }
    }

    /**
     * Gets the data encryption key.
     *
     * @return the data encryption key
     */
    @Override
    public String getDataEncryptionKey() {
        return config.aesEncryptionkey();
    }

    /**
     * Encrypt text.
     *
     * @param plainText
     *            the plain text @return the string @throws NoSuchPaddingException @throws
     *            NoSuchAlgorithmException @throws
     * @return the string
     */
    @Override
    public String encryptText(String plainText) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            LOGGER.error("Error in DataEncryptionService while encryption {}", e.getMessage());
        }
        return PWConstants.STATUS_ERROR;
    }

    /**
     * Decrypt text.
     *
     * @param encryptedText
     *            the encrypted text
     * @return the string
     */
    @Override
    public String decryptText(String encryptedText) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            LOGGER.error("Error in DataEncryptionService while decryption {}", e.getMessage());
        }
        return PWConstants.STATUS_ERROR;
    }

}
