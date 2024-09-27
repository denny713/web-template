package com.ndp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class EncryptUtil implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        try {
            return encrypt(charSequence.toString());
        } catch (Exception e) {
            log.error("Failed to encode the password: {}", e.getMessage());
            return "";
        }
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return this.encode(charSequence.toString()).equals(s);
    }

    public static String encrypt(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(text.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = digest.digest();
            StringBuilder str = new StringBuilder();
            for (byte byt : bytes) {
                str.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
            }
            return str.toString();
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }
}
