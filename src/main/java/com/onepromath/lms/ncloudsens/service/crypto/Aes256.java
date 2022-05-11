package com.onepromath.lms.ncloudsens.service.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class Aes256 {
    private String algorithm = "AES/CBC/PKCS5Padding";
    @Value("${crypto.secretKey}")
    private String secretKey;

    @Value("${crypto.iv}")
    private String iv; // 16 byte

    public String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance(this.algorithm);
        SecretKeySpec keySpec = new SecretKeySpec(this.secretKey.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(this.iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(this.algorithm);
        SecretKeySpec keySpec = new SecretKeySpec(this.secretKey.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(this.iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }
}
