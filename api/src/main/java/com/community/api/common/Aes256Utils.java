package com.community.api.common;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class Aes256Utils {
    private Aes256Utils() {

    }

    //    @Value("${key.dbSecurity}")
    final static String dbSecurityKey = "사과좋아바나나도좋아";

    public static String encrypt(final String data) {
        try {
            byte[] keyData = dbSecurityKey.getBytes(StandardCharsets.UTF_8);
            byte[] ivData = dbSecurityKey.substring(0, 16).getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyData, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivData));
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            log.error(String.valueOf(e));
        }
        return data;
    }

    public static String decrypy(final String data) {
        try {
            byte[] keyData = dbSecurityKey.getBytes(StandardCharsets.UTF_8);
            byte[] ivData = dbSecurityKey.substring(0, 16).getBytes(StandardCharsets.UTF_8);
            SecretKey secretKey = new SecretKeySpec(keyData, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(ivData));
            byte[] decrypted = Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8));
            return new String(cipher.doFinal(decrypted), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException |
                InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            log.error(String.valueOf(e));
        }
        return data;
    }
}

