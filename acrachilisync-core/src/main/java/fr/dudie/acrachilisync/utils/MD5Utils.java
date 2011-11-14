/*
 * Copyright (C) 2011 Jeremie Huchet
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
