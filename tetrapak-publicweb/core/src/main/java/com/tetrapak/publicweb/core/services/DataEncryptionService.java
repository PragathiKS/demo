package com.tetrapak.publicweb.core.services;


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
