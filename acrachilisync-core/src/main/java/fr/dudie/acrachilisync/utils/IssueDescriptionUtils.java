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
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.model.AcraReport;
import fr.dudie.acrachilisync.model.AcraReportHeader;
import fr.dudie.acrachilisync.model.ErrorOccurrence;

/**
 * Utilities to parse/build issue description.
 * <p>
 * Issue description example:
 * 
 * <pre>
 * h2. ACRA occurrences
 * |_. ACRA report id|_. crash date|_. run for|_. android|_\2. app version|_. device|
 * |<acra report id1>|dd/MM/yyyy hh:mm:ss|2s|1.6|12|0.3.1|Dream / HTC / dream|
 * |<acra report id435>|dd/MM/yyyy hh:mm:ss|1d 12m 32s|2.2|12|0.3.1|Nexus One / google / passion|
 * |<acra report id..>|dd/MM/yyyy hh:mm:ss|5m 6s|2.3.3|12|0.3.1|Nexus One / google / passion|
 * 
 * h2. Stacktrace
 * &lt;pre&gt;java.lang.NoClassDefFoundError: javax/mail/MessagingException
 *     at fr.dudie.acrachilisync.TestGdocsApi.listAllDocuments(TestGdocsApi.java:30)
 *     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
 * Caused by: java.lang.ClassNotFoundException: javax.mail.MessagingException
 *     at java.net.URLClassLoader$1.run(URLClassLoader.java:202)
 *     at java.lang.ClassLoader.loadClass(ClassLoader.java:247)
 *     ... 24 more&lt;/pre&gt;
 * </pre>
 * 
 * @author Jérémie Huchet
 */
public final class IssueDescriptionUtils {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDescriptionUtils.class);

    /**
     * Private constructor to avoid instantiation.
     */
    private IssueDescriptionUtils() {

    }

    /** ACRA bug occurrences table header. */
    private static final String OCCURRENCES_TABLE_HEADER = "|_. ACRA report id|_. crash date|_. run for|_. android|_\\2. app version|_. device|";

    /** ACRA bug occurrences table line format (use with {@link String#format(String, Object...)}). */
    private static final String OCCURRENCES_TABLE_LINE_FORMAT = "|%s|%s|%s|%s|%s|%s|%s|";

    /** The date format used for ACRA user crash dates. */
    private static final String OCCURRENCE_DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";

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
     * @param pError
     *            the ACRA error informations
     * @return a line for the ACRA bug occurrences table
     */
    public static Object getOccurrencesTableLine(final ErrorOccurrence pError) {

        final SimpleDateFormat format = new SimpleDateFormat(OCCURRENCE_DATE_FORMAT);
        return String.format(OCCURRENCES_TABLE_LINE_FORMAT, pError.getReportId(),
                format.format(pError.getCrashDate()),
                RunningTimeUtils.toString(pError.getRunFor()), pError.getAndroidVersion(),
                pError.getVersionCode(), pError.getVersionName(), pError.getDevice());
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

    /** Regular expression to detect line terminators. */
    public static final String EOL = "[\n(?:\r\n)\r\u0085\u2028\u2029]";

    /**
     * Converts an {@link AcraReport} to an {@link ErrorOccurrence}.
     * 
     * @param pReport
     *            the report to convert
     * @return the {@link ErrorOccurrence}
     */
    public static ErrorOccurrence toErrorOccurrence(final AcraReport pReport) {

        final ErrorOccurrence error = new ErrorOccurrence();
        error.setReportId(pReport.getId());
        error.setCrashDate(pReport.getUserCrashDate());
        error.setRunFor(new Date(pReport.getUserCrashDate().getTime()
                - pReport.getUserAppStartDate().getTime()));
        error.setAndroidVersion(pReport.getValue(AcraReportHeader.ANDROID_VERSION));
        error.setVersionCode(pReport.getValue(AcraReportHeader.APP_VERSION_CODE));
        error.setVersionName(pReport.getValue(AcraReportHeader.APP_VERSION_NAME));
        final StringBuilder device = new StringBuilder();
        device.append(pReport.getValue(AcraReportHeader.PHONE_MODEL)).append(" / ");
        device.append(pReport.getValue(AcraReportHeader.BRAND)).append(" / ");
        device.append(pReport.getValue(AcraReportHeader.PRODUCT));
        error.setDevice(device.toString());
        return error;
    }

    /** The current description format version. */
    public static final int DESCRIPTION_VERSION = 2;

    /** The expected description version tag marking. */
    public static final String DESCRIPTION_VERSION_TAG = String.format(
            "%%(acrachilisync-description-version)description_version_%d%%", DESCRIPTION_VERSION);

}
