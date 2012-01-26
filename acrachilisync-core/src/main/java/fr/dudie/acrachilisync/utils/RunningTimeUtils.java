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

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RunningTimeUtils {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RunningTimeUtils.class);

    private static final int SECOND = 1000;

    private static final int MINUTE = 60 * SECOND;

    private static final int HOUR = 60 * MINUTE;

    private static final int DAY = 24 * HOUR;

    private RunningTimeUtils() {

    }

    /**
     * Parse the given time using the {@link #OCCURRENCE_RUN_FORMAT} format.
     * 
     * @param pTime
     * @return a {@link Date} representing the given time
     * @throws ParseException
     *             date malformed
     */
    public static Date parseRunningTime(final String pTime) throws ParseException {

        int time = 0;
        Matcher m = Pattern.compile("(\\d+)d").matcher(pTime);
        if (m.find()) {
            time += Integer.parseInt(m.group(1)) * DAY;
        }
        m = Pattern.compile("(\\d+)h").matcher(pTime);
        if (m.find()) {
            time += Integer.parseInt(m.group(1)) * HOUR;
        }
        m = Pattern.compile("(\\d+)m").matcher(pTime);
        if (m.find()) {
            time += Integer.parseInt(m.group(1)) * MINUTE;
        }
        m = Pattern.compile("(\\d+)s").matcher(pTime);
        if (m.find()) {
            time += Integer.parseInt(m.group(1)) * SECOND;
        }

        return new Date(time);
    }

    public static String toString(final Date pTime) {

        final long t;
        if (null == pTime) {
            t = 0;
        } else {
            t = pTime.getTime();
        }

        final long days = t / DAY;
        final long hours = (t - days * DAY) / HOUR;
        final long minutes = (t - days * DAY - hours * HOUR) / MINUTE;
        final long seconds = (t - days * DAY - hours * HOUR - minutes * MINUTE) / SECOND;

        final StringBuilder buf = new StringBuilder();

        if (days != 0) {
            buf.append(days).append('d');
        }
        if (buf.length() != 0 || hours != 0) {
            if (buf.length() != 0) {
                buf.append(' ');
            }
            buf.append(hours).append('h');
        }
        if (buf.length() != 0 || minutes != 0) {
            if (buf.length() != 0) {
                buf.append(' ');
            }
            buf.append(minutes).append('m');
        }
        if (buf.length() != 0 || seconds != 0) {
            if (buf.length() != 0) {
                buf.append(' ');
            }
            buf.append(seconds).append('s');
        }

        if (buf.length() == 0) {
            buf.append("0s");
        }

        return buf.toString();
    }
}
