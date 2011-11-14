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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test the issue description reader.
 * 
 * @author Jérémie Huchet
 */
public final class IssueDescriptionBuilderTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDescriptionBuilderTest.class);

    /** A file containing a stacktrace. */
    private static final String STACK_SOURCE_FILE = "/files/md5utilstest_fe2d424fe0cb2b241de18c0e5f57e300.txt";

    /** The stacktrace. */
    private String stacktrace;

    /** The MD5 checksum of the stacktrace. */
    private String md5;

    /**
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {

        final BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass()
                .getResourceAsStream(STACK_SOURCE_FILE)));
        final StringBuilder stackBuf = new StringBuilder();

        final char[] cbuf = new char[512];
        int len;
        while ((len = reader.read(cbuf)) != -1) {
            stackBuf.append(cbuf, 0, len);
        }

        stacktrace = stackBuf.toString();
    }

    /**
     * Test build an issue description.
     */
    @Test
    public void testGenerateDescription() {

        final IssueDescriptionBuilder builder = new IssueDescriptionBuilder(stacktrace);
        builder.addOccurrence(UUID.randomUUID().toString(), new Date());
        final String description = builder.build();

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(description);
        }

        assertNotNull("check the generated description is not null", description);
    }
}
