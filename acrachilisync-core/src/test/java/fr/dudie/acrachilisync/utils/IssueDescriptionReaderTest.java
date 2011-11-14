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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.redmine.ta.beans.CustomField;
import org.redmine.ta.beans.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.exception.IssueParseException;

/**
 * Test the issue description reader.
 * 
 * @author Jérémie Huchet
 */
@RunWith(Parameterized.class)
public final class IssueDescriptionReaderTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDescriptionReaderTest.class);

    /** The test configuration. */
    private static ConfigurationManager configuration;

    /** The test filename. */
    private final String filename;

    /** The description to parse. */
    private final String description;

    /** The stacktraceMD5 field value the issue should have. */
    private final String stackMD5;

    /** True if a parse exception is expected. */
    private final boolean expectParseException;

    /**
     * Load the application test configuration before the tests.
     * 
     * @throws ConfigurationException
     *             the configuration cannot be loaded
     */
    @BeforeClass
    public static void loadConfiguration() throws ConfigurationException {

        configuration = ConfigurationManager.getInstance(new PropertiesConfiguration(
                "acrachilisync.properties"));
    }

    /**
     * Constructor.
     * 
     * @param pFile
     *            the file containing the description to parse
     * @param pExpectParseException
     *            true if a parse exception is expected during parsing the description the given
     *            file contains
     * @throws IOException
     *             unable to read test file
     */
    public IssueDescriptionReaderTest(final File pFile, final boolean pExpectParseException)
            throws IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialize test for file {}", pFile.getAbsolutePath());
        }
        expectParseException = pExpectParseException;

        final StringBuilder descriptionBuf = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
                pFile)));

        final char[] cbuf = new char[512];
        int len;
        while ((len = reader.read(cbuf)) != -1) {
            descriptionBuf.append(cbuf, 0, len);
        }

        description = descriptionBuf.toString();
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Description to parse: \n{}", description);
        }

        stackMD5 = pFile.getName().replaceAll("^descriptionreader_[ok]{2}_", "")
                .replaceAll("(_\\w+)?\\.txt$", "");
        filename = pFile.getName();
    }

    /**
     * Generates the test dataset.
     * <p>
     * List all files under classpath:/files/description_[ko]{2}_.*.txt.
     * 
     * @return the test dataset
     * @throws URISyntaxException
     *             urisyntexception
     */
    @Parameters
    public static List<Object[]> data() throws URISyntaxException {

        final File files = new File(MD5UtilsTest.class.getResource("/files").toURI());
        final Pattern p = Pattern.compile("descriptionreader_[ko]{2}_.*\\.txt",
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
            data.add(new Object[] { f, !f.getName().startsWith("descriptionreader_ok") });
        }
        return data;
    }

    /**
     * Parse the file content and check if an exception is thrown depending on
     * {@link #expectParseException}.
     */
    @Test
    public void testParseDescription() {

        final Issue issue = new Issue();
        issue.setId(1);
        issue.setDescription(description);
        final CustomField stacktraceMD5 = new CustomField();
        stacktraceMD5.setId(configuration.CHILIPROJECT_STACKTRACE_MD5_CF_ID);
        stacktraceMD5.setValue(stackMD5);
        issue.getCustomFields().add(stacktraceMD5);

        Exception error = null;
        IssueDescriptionReader reader = null;
        try {
            reader = new IssueDescriptionReader(issue);
        } catch (final IssueParseException e) {
            error = e;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("", e);
            }
        }

        if (LOGGER.isInfoEnabled()) {
            if (expectParseException && null != error) {
                LOGGER.info("Got expected parse exception");
            } else if (!expectParseException && null == error) {
                LOGGER.info("No parse exception thrown as expected");
            }
        }
        if (expectParseException && null == error) {
            LOGGER.error("Missing parse exception");
        } else if (!expectParseException && null != error) {
            LOGGER.error("Got a parse exception");
        }

        if (expectParseException) {
            assertNotNull("check a parse exception occurred: " + filename, error);
        } else {
            assertNull("check no exception occurred: " + filename, error);
        }
    }
}
