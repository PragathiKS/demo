package com.tetrapak.publicweb.core.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

// TODO: Auto-generated Javadoc
/**
 * The Interface DataEncryptionService.
 */
public interface DataEncryptionService {

    /**
     * Gets the data encryption key.
     *
     * @return the data encryption key
     */
    String getDataEncryptionKey();

    /**
     * Encrypt text.
     *
     * @param plainText
     *            the plain text
     * @return the string
     */
    String encryptText(String plainText);

    /**
     * Decrypt text.
     *
     * @param encryptedText
     *            the encrypted text
     * @return the string
     */
    String decryptText(String encryptedText);

}
