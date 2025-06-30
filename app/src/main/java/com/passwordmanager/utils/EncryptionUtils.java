//Menyediakan metode enkripsi/dekripsi password (misalnya AES).
package com.passwordmanager.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

/**
 * Utility class untuk enkripsi/dekripsi AES (SRS NF001.2-3).
 * Menggunakan AES-256 dengan mode ECB dan PKCS5Padding.
 */
public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final int KEY_LENGTH = 32; // 256-bit key

    /**
     * Enkripsi data dengan AES-256 (SRS NF001.2).
     *
     * @param data Plaintext yang akan dienkripsi
     * @param key  Base64-encoded encryption key
     * @return Base64-encoded ciphertext
     */
    public static String encrypt(String data, String key) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(key);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new SecurityException("Encryption failed", e);
        }
    }

    /**
     * Dekripsi AES-256 (SRS NF001.2)
     *
     * @param encryptedData Base64-encoded ciphertext
     * @param key           Base64-encoded encryption key
     * @return Plaintext
     */
    public static String decrypt(String encryptedData, String key) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(key);
            SecretKeySpec secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedData);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SecurityException("Decryption failed", e);
        }
    }

    /**
     * Generate random AES-256 key untuk tiap password entry (SRS NF001.3).
     *
     * @return Base64-encoded encryption key
     */
    public static String generateRandomKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[KEY_LENGTH];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    /**
     * Generate SHA-256 hash dari password user.
     *
     * @param password plain password
     * @return Base64-encoded SHA-256 hash
     */
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new SecurityException("Hashing failed", e);
        }
    }
}
