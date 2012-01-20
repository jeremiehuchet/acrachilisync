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

import static org.junit.Assert.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parameterized.class)
public class DescriptionVersionPatternTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DescriptionVersionPatternTest.class);

    @Parameters
    public static List<Object[]> data() throws URISyntaxException {

        final List<Object[]> data = new ArrayList<Object[]>();
        data.add(new Object[] { true, "%{visibility:hidden}description_version_2%" });
        data.add(new Object[] { true, "  %{visibility:hidden}description_version_2%  " });
        data.add(new Object[] { true, "%{visibility:hidden}description_version_2%" + '\n' });
        data.add(new Object[] { false, "" });

        return data;
    }

    private final Pattern p = IssueDescriptionReader.DESCRIPTION_VERSION_PATTERN;

    private final String line;

    private final boolean result;

    public DescriptionVersionPatternTest(final boolean pResult, final String pLine) {

        this.line = pLine;
        this.result = pResult;
    }

    @Test
    public void checkPatternMatches() {

        assertEquals(result, p.matcher(line).matches());
    }

    @Test
    public void checkPatternFind() {

        assertEquals(result, p.matcher(line).find());
    }
}
