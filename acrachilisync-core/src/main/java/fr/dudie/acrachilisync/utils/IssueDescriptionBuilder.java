package fr.dudie.acrachilisync.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to build a formatted issue description to be able to retrieve informations later.
 * 
 * @author Jérémie Huchet
 * @see IssueDescriptionReader
 */
public final class IssueDescriptionBuilder {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDescriptionBuilder.class);

    /** The error stacktrace. */
    private final String stacktrace;

    /** The error stacktrace MD5 hash. */
    private final String stacktraceMD5;

    /** The occurrence time for each ACRA bug report. */
    private final Map<String, Date> occurrences;

    /**
     * Default constructor.
     * 
     * @param pStacktrace
     *            the stacktrace
     */
    public IssueDescriptionBuilder(final String pStacktrace) {

        stacktrace = pStacktrace;
        stacktraceMD5 = MD5Utils.toMD5hash(pStacktrace);
        occurrences = new HashMap<String, Date>();
    }

    /**
     * Builds the description.
     * 
     * @return the issue description
     * @see IssueDescriptionUtils
     */
    public String build() {

        final StringBuilder description = new StringBuilder();
        // append occurrences
        description.append(IssueDescriptionUtils.getOccurrencesTableHeader()).append("\n");
        for (final Entry<String, Date> entry : occurrences.entrySet()) {
            description.append(IssueDescriptionUtils.getOccurrencesTableLine(entry.getKey(),
                    entry.getValue()));
            description.append("\n");
        }

        // append stacktrace
        description.append("\n\n");
        description.append("*Stacktrace*").append("\n");
        description.append("<pre class=\"javastacktrace\">");
        description.append(stacktrace.trim()).append("</pre>");

        return description.toString();
    }

    /**
     * Sets the list of occurrences for this bug.
     * 
     * @param pOccurrences
     *            a map with the bug occurrence date for each ACRA report id related to the bug
     * @see #addOccurrence(String, Date)
     */
    public void setOccurrences(final Map<String, Date> pOccurrences) {

        occurrences.clear();
        for (final Entry<String, Date> entry : pOccurrences.entrySet()) {
            addOccurrence(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds an occurrence for this bug.
     * 
     * @param pReportId
     *            the ACRA report id
     * @param pUserCrashDate
     *            the date when the user crash occurred
     */
    public void addOccurrence(final String pReportId, final Date pUserCrashDate) {

        occurrences.put(pReportId, pUserCrashDate);
    }
}
