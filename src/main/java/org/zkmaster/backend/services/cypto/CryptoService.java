package org.zkmaster.backend.services.cypto;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.exceptions.CryptoFailException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

/**
 * Encode & decode value service.
 *
 * @author Daniils Loputevs.
 * @since 09.02.2021.
 */
@Component
public class CryptoService {
    private final int iterationCount = 10;
    private final String algorithm = "PBEWithMD5AndDes";
    private final String coding = "UTF8";
    private final byte[] salt = new byte[]{100, 105, 109, 97, 107, 118, 111, 110};
    
    private final String secretKey = "yuri";
    private final String secretKeyCrypted = cryptSecretKey();
    
    public CryptoService() throws Exception {}
    
    
    public String encode(String text) throws Exception {
        try {
            return enc(text, secretKeyCrypted);
        } catch (Exception origException) {
            origException.printStackTrace();
            throw new CryptoFailException(text);
        }
    }
    
    public String decode(String text) throws Exception {
        try {
            return dec(text, secretKeyCrypted);
        } catch (Exception origException) {
            origException.printStackTrace();
            throw new CryptoFailException(text);
        }
    }
    
    
    private SecretKey genSecretKey(String key) throws Exception {
        KeySpec keySpec = new PBEKeySpec(key.toCharArray(), salt, 10);
        return SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec);
    }
    
    
    private String dec(String hash, String stringKey) throws Exception {
        SecretKey secretKey = getSecretKey(stringKey);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(2, secretKey, paramSpec);
        byte[] decHash = Base64.decodeBase64(hash);
        return new String(cipher.doFinal(decHash), coding);
    }
    
    private String enc(String text, String stringKey) throws Exception {
        SecretKey secretKey = getSecretKey(stringKey);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(1, secretKey, paramSpec);
        byte[] encText = cipher.doFinal(text.getBytes(coding));
        return new String(Base64.encodeBase64(encText));
    }
    
    /**
     * toString() for {@link SecretKey}
     *
     * @param secretKey -
     * @return -
     */
    private String secretKeyToString(SecretKey secretKey) {
        return new String(Base64.encodeBase64(secretKey.getEncoded()));
    }
    
    /**
     * String stringKey -> SecretKey obj
     *
     * @param stringKey -
     * @return -
     */
    private SecretKey getSecretKey(String stringKey) {
        return new SecretKeySpec(Base64.decodeBase64(stringKey), algorithm);
    }
    
    private String cryptSecretKey() throws Exception {
        return secretKeyToString(genSecretKey(secretKey));
    }
    
}
