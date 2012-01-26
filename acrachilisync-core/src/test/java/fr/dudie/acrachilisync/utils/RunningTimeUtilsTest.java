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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunningTimeUtilsTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RunningTimeUtilsTest.class);

    private static final int S = 1000;

    private static final int M = 60 * S;

    private static final int H = 60 * M;

    private static final int D = 24 * H;

    public static List<Object[]> data() throws URISyntaxException {

        final List<Object[]> data = new ArrayList<Object[]>();

        data.add(new Object[] { H + 10 * S, "1h 10s" });
        data.add(new Object[] { 10 * S, "0h 10s" });
        data.add(new Object[] { D + H + M, "1d 1h 1m" });

        data.add(new Object[] { H + M + S, "0d 1h 1m 1s" });
        data.add(new Object[] { D + H + M + S, "1d 1h 1m 1s" });

        for (int d = 0; d < 14; d += 3) {
            for (int h = 0; h < 24; h += 3) {
                for (int m = 0; m < 60; m += 6) {
                    for (int s = 0; s < 60; s += 5) {
                        data.add(new Object[] { d * D + h * H + m * M + s * S,
                                String.format("%dd %dh %dm %ds", d, h, m, s) });
                    }
                }
                LOGGER.info("generated dataset for d={} and h={}", d, h);
            }
        }

        return data;
    }

    @Test
    public void checkParsedTime() throws ParseException {

        assertEquals("1h 10s", H + 10 * S, RunningTimeUtils.parseRunningTime("1h 10s").getTime());
        assertEquals("0h 10s", 10 * S, RunningTimeUtils.parseRunningTime("0h 10s").getTime());
        assertEquals("1d 1h 1m", D + H + M, RunningTimeUtils.parseRunningTime("1d 1h 1m").getTime());
        assertEquals("0d 1h 1m 1s", H + M + S, RunningTimeUtils.parseRunningTime("0d 1h 1m 1s")
                .getTime());
        assertEquals("1d 1h 1m 1s", D + H + M + S, RunningTimeUtils.parseRunningTime("1d 1h 1m 1s")
                .getTime());

        for (int d = 0; d < 12; d++) {
            for (int h = 0; h < 24; h++) {
                for (int m = 0; m < 60; m++) {
                    for (int s = 0; s < 60; s++) {
                        final int expected = d * D + h * H + m * M + s * S;
                        final String value = String.format("%dd %dh %dm %ds", d, h, m, s);
                        assertEquals(value, expected, RunningTimeUtils.parseRunningTime(value)
                                .getTime());
                    }
                }
            }
            LOGGER.info("day {}/11 finished", d);
        }
    }

    @Test
    public void checkToString() throws ParseException {

        assertEquals("1d 1h 1m 1s", "1d 1h 1m 1s",
                RunningTimeUtils.toString(new Date(D + H + M + S)));
        assertEquals("1h 1m 1s", "1h 1m 1s", RunningTimeUtils.toString(new Date(H + M + S)));
        assertEquals("1m 1s", "1m 1s", RunningTimeUtils.toString(new Date(M + S)));
        assertEquals("1s", "1s", RunningTimeUtils.toString(new Date(S)));

        assertEquals("8d 2h 54m 41s", "8d 2h 54m 41s",
                RunningTimeUtils.toString(new Date(8 * D + 2 * H + 54 * M + 41 * S)));

        assertEquals("15d 0h 0m 43s", "15d 0h 0m 43s",
                RunningTimeUtils.toString(new Date(15 * D + 43 * S)));

        assertEquals("0s", "0s", RunningTimeUtils.toString(new Date(0)));

        assertEquals("0d 2h 0s", "2h 0m 0s", RunningTimeUtils.toString(new Date(2 * H)));
    }
}
