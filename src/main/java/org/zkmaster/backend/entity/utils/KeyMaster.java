package org.zkmaster.backend.entity.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

public class KeyMaster {
    private static final int iterationCount = 10;
    private static final String algorithm = "PBEWithMD5AndDes";
    private static final String coding = "UTF8";
    private static final byte[] salt = new byte[]{100, 105, 109, 97, 107, 118, 111, 110};
   
    
    public static SecretKey genSecretKey(String key) throws Exception {
        KeySpec keySpec = new PBEKeySpec(key.toCharArray(), salt, 10);
        return SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec);
    }
    
    
    public static String dec(String hash, String stringKey) throws Exception {
        SecretKey secretKey = getSecretKey(stringKey);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(2, secretKey, paramSpec);
        byte[] decHash = Base64.decodeBase64(hash);
        return new String(cipher.doFinal(decHash), coding);
    }
    
    public static String enc(String text, String stringKey) throws Exception {
        SecretKey secretKey = getSecretKey(stringKey);
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(1, secretKey, paramSpec);
        byte[] encText = cipher.doFinal(text.getBytes(coding));
        return new String(Base64.encodeBase64(encText));
    }
    
    /**
     * toString() for {@link SecretKey}
     * @param secretKey -
     * @return -
     */
    public static String secretKeyToString(SecretKey secretKey) {
        return new String(Base64.encodeBase64(secretKey.getEncoded()));
    }
    
    /**
     * String stringKey -> SecretKey obj
     *
     * @param stringKey -
     * @return -
     */
    private static SecretKey getSecretKey(String stringKey) {
        return new SecretKeySpec(Base64.decodeBase64(stringKey), algorithm);
    }
    
}
