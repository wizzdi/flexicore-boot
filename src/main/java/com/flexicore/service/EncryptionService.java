package com.flexicore.service;

import com.flexicore.interfaces.FlexiCoreService;

import java.security.GeneralSecurityException;

public interface EncryptionService extends FlexiCoreService {
    /**
     * encrypt data
     * @param plaintext data to encrypt
     * @param associatedData salt
     * @return encrypted data
     * @throws GeneralSecurityException
     */
    byte[] encrypt(byte[] plaintext, byte[] associatedData) throws GeneralSecurityException;

    /**
     * decrypt data
     * @param ciphertext encrypted data
     * @param associatedData salt used when encrypted
     * @return decrypted data
     * @throws GeneralSecurityException
     */
    byte[] decrypt(byte[] ciphertext, byte[] associatedData) throws GeneralSecurityException;
}
