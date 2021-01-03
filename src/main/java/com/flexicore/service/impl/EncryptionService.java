package com.flexicore.service.impl;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AesGcmKeyManager;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.concurrent.atomic.AtomicBoolean;


@Primary
@Component
@Extension
public class EncryptionService implements com.flexicore.service.EncryptionService {
    private static Aead aead;

    private  static final Logger logger=LoggerFactory.getLogger(EncryptionService.class);

    @Value("${flexicore.security.encryption.tinkKeySetPath:/home/flexicore/keyset.json}")
    private String tinkKeySetPath;



    private static AtomicBoolean init=new AtomicBoolean(false);


    private void initEncryption() {
        if(init.compareAndSet(false,true)){
            try {
                AeadConfig.register();
                File keysetFile = new File(tinkKeySetPath);
                KeysetHandle keysetHandle;
                if (!keysetFile.exists()) {

                    KeyTemplate keyTemplate = AesGcmKeyManager.aes256GcmTemplate();
                    keysetHandle = KeysetHandle.generateNew(keyTemplate);
                    CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(keysetFile));
                } else {
                    keysetHandle = CleartextKeysetHandle.read(JsonKeysetReader.withFile(keysetFile));
                }
                aead = keysetHandle.getPrimitive(Aead.class);


            } catch (Exception e) {
                logger.error( "failed loading keyHandle", e);
            }

        }


    }

    @Override
    public byte[] encrypt(final byte[] plaintext, final byte[] associatedData) throws GeneralSecurityException{
        initEncryption();
        return aead.encrypt(plaintext,associatedData);

    }

    @Override
    public byte[] decrypt(final byte[] ciphertext, final byte[] associatedData) throws GeneralSecurityException{
        initEncryption();
        return aead.decrypt(ciphertext,associatedData);
    }

}
