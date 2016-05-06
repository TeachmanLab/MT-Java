package edu.virginia.psyc.mindtrails.service;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Useful for encrypting data with a Public key, that can only be recovered with a
 * private key that should be stored elsewhere outside the application.
 */
@Service
public class RsaEncryptionService {

    private static final Logger LOG = LoggerFactory.getLogger(RsaEncryptionService.class);
    private static final String ALGORITHM= "RSA";

    @Value("${encryption.enabled}")
    private String enabledString;

    @Autowired
    ResourceLoader resourceLoader;

    private PublicKey publicKey;

    // Loads the public key from a file in resources.
    @PostConstruct
    public void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        LOG.info("Loading encryption public key");
        ObjectInputStream inputStream = null;
        Resource resource = resourceLoader.getResource("classpath:public_key.der");
        LOG.info("Loading encryption public key.  The Resource is:" + resource.getFile().getAbsolutePath());
        File publicKeyFile = resource.getFile();
        publicKey = getPublicKey(publicKeyFile);
    }

    public static PublicKey getPublicKey(File f) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
        return kf.generatePublic(spec);
    }

    public String encryptIfEnabled(int number) { return encryptIfEnabled("" + number); }
    public String encryptIfEnabled(long number) { return encryptIfEnabled("" + number); }

    public String encryptIfEnabled(String text) {
        boolean enabled = Boolean.parseBoolean(enabledString);
        // If encryption is not enabled, then don't actually encrypt the string.
        if(!enabled) return text;
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(cipherText);
    }

}
