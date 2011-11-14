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

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test MD5 utils.
 * 
 * @author Jérémie Huchet
 */
@RunWith(Parameterized.class)
public final class MD5UtilsTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MD5UtilsTest.class);

    /** The expected MD5 checksum. */
    private final String expectedMD5Checksum;

    /** The file to compute the MD5 checksum. */
    private final File file;

    /**
     * Constructor.
     * 
     * @param pExpectedMD5Checksum
     *            the expected MD5 checksum
     * @param pFile
     *            the file to compute the MD5 checksum
     */
    public MD5UtilsTest(final String pExpectedMD5Checksum, final File pFile) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialize test for file {}", pFile.getAbsolutePath());
        }
        expectedMD5Checksum = pExpectedMD5Checksum;
        file = pFile;
    }

    /**
     * Generates the test dataset.
     * <p>
     * List all files under classpath:/files/stacktrace_[a-z0-9]{32}.txt.
     * 
     * @return the test dataset
     * @throws URISyntaxException
     *             urisyntexception
     */
    @Parameters
    public static List<Object[]> data() throws URISyntaxException {

        final File files = new File(MD5UtilsTest.class.getResource("/files").toURI());
        final Pattern p = Pattern.compile("md5utilstest_[a-f0-9]{32}\\.txt",
                Pattern.CASE_INSENSITIVE);
        final FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {

                final Matcher m = p.matcher(name);
                return m.matches();
            }
        };

        final List<Object[]> data = new ArrayList<Object[]>();
        for (final File f : files.listFiles(filter)) {
            data.add(new Object[] { f.getName().replaceAll("(^md5utilstest_)|(\\.txt)$", ""), f });
        }
        return data;
    }

    /**
     * Computes the MD5 hash for the file and check the result value.
     * 
     * @throws IOException
     *             ioexception
     */
    @Test
    public void testToMD5hash() throws IOException {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                file)));
        final StringBuilder stacktrace = new StringBuilder();

        final char[] cbuf = new char[512];
        int len;
        while ((len = reader.read(cbuf)) != -1) {
            stacktrace.append(cbuf, 0, len);
        }
        assertEquals("check MD5 checksum for file: " + file.getName(), expectedMD5Checksum,
                MD5Utils.toMD5hash(stacktrace.toString()));
    }
}
