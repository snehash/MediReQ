package com.example.sneha.medireq;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    /**
     * Take String and encrypt, output base64 encoded String
     * @param origData original data to be encrypted
     * @param key password for decrypting data
     * @return base64 encoded encrypted data
     */
    public static String encrypt(String origData, String key) {
        byte[] hash = sha1(key);
        if (hash == null) { return null; }

        SecretKeySpec secretKey = new SecretKeySpec(hash, "AES");

        byte[] encryptedData = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedData = c.doFinal(origData.getBytes("UTF-8"));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (encryptedData == null) { return null; }

        return Base64.encodeToString(encryptedData, Base64.NO_PADDING);
    }

    public static Cipher getEncryptCipher(String key) {
        byte[] hash = sha1(key);
        if (hash == null) { return null; }

        SecretKeySpec secretKey = new SecretKeySpec(hash, "AES");

        byte[] encryptedData = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, secretKey);
            return c;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Decrypt base64 AES encrypted String
     * @param encryptedData encrypted data encoded in base64 to be decrypted
     * @param key password for decrypting
     * @return original data decrypted
     */
    public static String decrypt(String encryptedData, String key) {
        byte[] hash = sha1(key);
        SecretKeySpec secretKey = new SecretKeySpec(hash, "AES");

        byte[] dataBytes = null;
        try {
            dataBytes = Base64.decode(encryptedData.getBytes("UTF-8"), Base64.NO_PADDING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Decode the encoded data with AES
        byte[] origData = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, secretKey);
            origData = c.doFinal(dataBytes);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        if (origData == null) { return null; }
        return new String(origData);
    }

    public static Cipher getDecryptCipher(String key) {
        byte[] hash = sha1(key);
        SecretKeySpec secretKey = new SecretKeySpec(hash, "AES");

        // Decode the encoded data with AES
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, secretKey);
            return c;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] sha1(String input) {
        MessageDigest crypt = null;
        try {
            crypt = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hash = null;
        crypt.reset();
        try {
            hash = crypt.digest(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hash;
    }
}
