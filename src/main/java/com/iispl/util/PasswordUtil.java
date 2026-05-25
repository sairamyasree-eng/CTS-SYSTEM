package com.iispl.util;

import com.iispl.exceptions.AuthException;

public class PasswordUtil {

    private PasswordUtil() {}

    public static String hash(String plainPassword) {
        try {
            java.security.MessageDigest md =
                java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(plainPassword.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new AuthException("Password hashing failed: " + e.getMessage());
        }
    }
}