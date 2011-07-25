package fr.dudie.acrachilisync.utils;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility to computes MD5 hash <b>on UTF-8 strings only</b>. Do not work with binary data.
 * 
 * @author Jérémie Huchet
 */
public final class MD5Utils {

    /**
     * Private constructor to avoid instantiation.
     */
    private MD5Utils() {

    }

    /**
     * Gets the MD5 checksum for the given string (using UTF-8 encoding).
     * 
     * @param s
     *            a string
     * @return the MD5 checksum for the given string
     */
    public static String toMD5hash(final String s) {

        String hash = null;
        if (null != s) {
            try {
                final MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(s.getBytes(Charset.forName("utf-8")));
                final byte[] md5sum = digest.digest();
                final BigInteger bigInt = new BigInteger(1, md5sum);
                hash = bigInt.toString(16);
            } catch (final NoSuchAlgorithmException e) {
                throw new RuntimeException("MD5 digest algorithm implementation not found", e);
            }
        }
        return hash;
    }
}
