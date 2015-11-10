package edu.virginia.psyc.pi.service;

import edu.virginia.psyc.pi.Application;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 7/22/14
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class RsaEncryptionServiceTest {

    @Autowired
    private RsaEncyptionService service;

    @Autowired
    ResourceLoader resourceLoader;

    public PrivateKey getPrivateKey() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:private_key.der");
        File f = resource.getFile();
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int)f.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Test
    public void testEncryptionSameEachTime() throws Exception {
        String cipherText1 = service.encrypt(100);
        String cipherText2 = service.encrypt(100);
        assertTrue(cipherText1 + " == " + cipherText2, cipherText1.equals(cipherText2));
    }

    @Test
    public void testEncryption() throws Exception {
        String cipherText = service.encrypt(100);
        System.out.println("encodedBytes: '" + cipherText + "'");

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        String decodedText = new String(cipher.doFinal(Base64.decodeBase64(cipherText)), "UTF-8");
        System.out.println("Decoded String'" + decodedText + "'");
    }


}
