package org.ioanntar.webproject.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {

    private final String password;

    public PasswordHash(String password) {
        this.password = password;
    }

    public String hash(String hashAlgorithm) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(hashAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = md.digest(password.getBytes());
        BigInteger integer = new BigInteger(1, bytes);
        return integer.toString(32);
    }
}
