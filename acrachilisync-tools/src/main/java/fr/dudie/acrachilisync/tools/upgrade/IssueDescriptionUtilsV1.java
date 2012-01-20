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

package fr.dudie.acrachilisync.tools.upgrade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities to parse/build issue description.
 * <p>
 * Issue description example:
 * 
 * <pre>
 * h2. ACRA occurrences
 * %{visibility:hidden}occurrences_73ad687b153f570e218c5b5c2226edc4_start%
 * |_. ACRA report id|_. date|
 * |<acra report id1>|dd/MM/yyyy hh:mm:ss|
 * |<acra report id435>|dd/MM/yyyy hh:mm:ss|
 * |<acra report id..>|dd/MM/yyyy hh:mm:ss|
 * %{visibility:hidden}occurrences_73ad687b153f570e218c5b5c2226edc4_end%
 * 
 * h2. Stacktrace
 * %{visibility:hidden}stacktrace_73ad687b153f570e218c5b5c2226edc4_start%
 * &lt;pre&gt;java.lang.NoClassDefFoundError: javax/mail/MessagingException
 * at fr.dudie.acrachilisync.TestGdocsApi.listAllDocuments(TestGdocsApi.java:30)
 * at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 * Caused by: java.lang.ClassNotFoundException: javax.mail.MessagingException
 * at java.net.URLClassLoader$1.run(URLClassLoader.java:202)
 * at java.lang.ClassLoader.loadClass(ClassLoader.java:247)
 * ... 24 more&lt;/pre&gt;
 * %{visibility:hidden}stacktrace_73ad687b153f570e218c5b5c2226edc4_end%
 * </pre>
 * 
 * @author Jérémie Huchet
 */
public final class IssueDescriptionUtilsV1 {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDescriptionUtilsV1.class);

    /**
     * Private constructor to avoid instantiation.
     */
    private IssueDescriptionUtilsV1() {

    }

    /**
     * Start tag format put just before the table containg ACRA bug occurrence dates (use with
     * {@link String#format(String, Object...)}).
     */
    private static final String OCCURRENCES_START_TAG_FORMAT = "%%{visibility:hidden}occurrences_%s_start%%";

    /**
     * End tag format put just after the table containg ACRA bug occurrence dates (use with
     * {@link String#format(String, Object...)}).
     */
    private static final String OCCURRENCES_END_TAG_FORMAT = "%%{visibility:hidden}occurrences_%s_end%%";

    /** ACRA bug occurrences table header. */
    private static final String OCCURRENCES_TABLE_HEADER = "|_. ACRA report id|_. date|";

    /** ACRA bug occurrences table line format (use with {@link String#format(String, Object...)}). */
    private static final String OCCURRENCES_TABLE_LINE_FORMAT = "|%s|%s|";

    /** The date format used for ACRA user crash dates. */
    private static final String OCCURRENCE_DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";

    /**
     * Gets the formatted {@link #OCCURRENCES_START_TAG_FORMAT}.
     * 
     * @param pStacktraceMD5
     *            a MD5 hash
     * @return a unique start tag for the given hash
     */
    public static String getOccurrencesStartTag(final String pStacktraceMD5) {

        return String.format(OCCURRENCES_START_TAG_FORMAT, pStacktraceMD5);
    }

    /**
     * Gets the formatted {@link #OCCURRENCES_END_TAG_FORMAT}.
     * 
     * @param pStacktraceMD5
     *            a MD5 hash
     * @return a unique end tag for the given hash
     */
    public static String getOccurrencesEndTag(final String pStacktraceMD5) {

        return String.format(OCCURRENCES_END_TAG_FORMAT, pStacktraceMD5);
    }

    /**
     * Gets the ACRA bug occurrences table header.
     * 
     * @return the ACRA bug occurrences table header
     */
    public static String getOccurrencesTableHeader() {

        return OCCURRENCES_TABLE_HEADER;
    }

    /**
     * Formats a table line for the ACRA bug occurrences table.
     * 
     * @param pAcraReportId
     *            the ACRA report id
     * @param pUserCrashDate
     *            the user crash date
     * @return a line for the ACRA bug occurrences table
     */
    public static String getOccurrencesTableLine(final String pAcraReportId,
            final Date pUserCrashDate) {

        final SimpleDateFormat format = new SimpleDateFormat(OCCURRENCE_DATE_FORMAT);
        return String.format(OCCURRENCES_TABLE_LINE_FORMAT, pAcraReportId,
                format.format(pUserCrashDate));
    }

    /**
     * Parse the given date using the {@link #OCCURRENCE_DATE_FORMAT} format.
     * 
     * @param pDate
     *            a string representation of a date
     * @return the date
     * @throws ParseException
     *             date malformed
     */
    public static Date parseDate(final String pDate) throws ParseException {

        final SimpleDateFormat format = new SimpleDateFormat(OCCURRENCE_DATE_FORMAT);
        return format.parse(pDate);
    }

    /**
     * Start tag format put just before the full stacktrace. (use with
     * {@link String#format(String, Object...)}).
     */
    private static final String STACKTRACE_START_TAG_FORMAT = "%%{visibility:hidden}stacktrace_%s_start%%";

    /**
     * End tag format put just after the full stacktrace. (use with
     * {@link String#format(String, Object...)}).
     */
    private static final String STACKTRACE_END_TAG_FORMAT = "%%{visibility:hidden}stacktrace_%s_end%%";

    /** Regular expression to detect line terminators. */
    public static final String EOL = "[\n(?:\r\n)\r\u0085\u2028\u2029]";

    /**
     * Gets the formatted {@link #STACKTRACE_START_TAG_FORMAT}.
     * 
     * @param pStacktraceMD5
     *            a MD5 hash
     * @return a unique start tag for the given hash
     */
    public static String getStacktraceStartTag(final String pStacktraceMD5) {

        return String.format(STACKTRACE_START_TAG_FORMAT, pStacktraceMD5);
    }

    /**
     * Gets the formatted {@link #STACKTRACE_END_TAG_FORMAT}.
     * 
     * @param pStacktraceMD5
     *            a MD5 hash
     * @return a unique end tag for the given hash
     */
    public static String getStacktraceEndTag(final String pStacktraceMD5) {

        return String.format(STACKTRACE_END_TAG_FORMAT, pStacktraceMD5);
    }
}
