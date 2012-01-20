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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.redmine.ta.beans.CustomField;
import org.redmine.ta.beans.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.exception.IssueParseException;
import fr.dudie.acrachilisync.utils.ConfigurationManager;
import fr.dudie.acrachilisync.utils.CustomFieldIdPredicate;

/**
 * Helper class to extract informations from an issue description generated with
 * IssueDescriptionBuilder.
 * 
 * @author Jérémie Huchet
 */
public final class IssueDescriptionReaderV1 {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDescriptionReaderV1.class);

    /** Regexp : <code>|jsglq4354gjdslg4434|dd/MM/yyyy hh:mm:ss|</code>. */
    private static final String OCCURR_LINE_PATTERN = "\\|[\\w-]+\\|\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}\\|";

    /** The error stacktrace. */
    private final String stacktrace;

    /** The error stacktrace MD5 hash. */
    private final String stacktraceMD5;

    /** The occurrence time for each ACRA bug report. */
    private final Map<String, Date> occurrences = new HashMap<String, Date>();

    /**
     * Constructor.
     * <p>
     * The description must have been formatted with IssueDescriptionBuilder in order to make this
     * reader working.
     * 
     * @param pIssue
     *            the issue
     * @throws IssueParseException
     *             unable to parse the issue
     */
    public IssueDescriptionReaderV1(final Issue pIssue) throws IssueParseException {

        stacktraceMD5 = getStacktraceMD5(pIssue);
        if (!StringUtils.isBlank(pIssue.getDescription())) {
            occurrences.putAll(parseAcraOccurrencesTable(pIssue.getDescription(), stacktraceMD5));
            stacktrace = parseStacktrace(pIssue.getDescription(), stacktraceMD5);
        } else {
            stacktrace = "";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Issue {} have an empty description", pIssue.getId());
            }
        }
    }

    /**
     * Extracts the stacktrace MD5 custom field value from the issue.
     * 
     * @param pIssue
     *            the issue
     * @return the stacktrace MD5
     * @throws IssueParseException
     *             issue doesn't contains a stack trace md5 custom field
     */
    private String getStacktraceMD5(final Issue pIssue) throws IssueParseException {

        final int stackCfId = ConfigurationManager.getInstance().CHILIPROJECT_STACKTRACE_MD5_CF_ID;
        final Predicate findStacktraceMD5 = new CustomFieldIdPredicate(stackCfId);
        final CustomField field = (CustomField) CollectionUtils.find(pIssue.getCustomFields(),
                findStacktraceMD5);
        if (null == field) {
            throw new IssueParseException(String.format(
                    "Issue %d doesn't contains a custom_field id=%d", pIssue.getId(), stackCfId));
        }
        return field.getValue();
    }

    /**
     * Extracts the list of bug occurrences from the description.
     * 
     * @param pDescription
     *            the issue description
     * @param pStacktraceMD5
     *            the stacktrace MD5 hash the issue is related to
     * @return the ACRA bug occurrences listed in the description
     * @throws IssueParseException
     *             malformed issue description
     */
    private Map<String, Date> parseAcraOccurrencesTable(final String pDescription,
            final String pStacktraceMD5) throws IssueParseException {

        final Map<String, Date> occur = new HashMap<String, Date>();

        // escape braces { and } to use strings in regexp
        final String header = IssueDescriptionUtilsV1.getOccurrencesTableHeader();
        final String escHeader = Pattern.quote(header);

        // regexp to find occurrences tables
        final Pattern p = Pattern.compile(escHeader + IssueDescriptionUtilsV1.EOL + "(?:"
                + OCCURR_LINE_PATTERN + IssueDescriptionUtilsV1.EOL + "+)+", Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(pDescription);

        if (m.find()) {
            // regexp to find occurrences lines
            final Pattern pLine = Pattern.compile(OCCURR_LINE_PATTERN);
            final Matcher mLine = pLine.matcher(m.group());
            while (mLine.find()) {
                final StringTokenizer line = new StringTokenizer(mLine.group(), "|");
                final String acraReportId = line.nextToken();
                final String acraUserCrashDate = line.nextToken();
                try {
                    occur.put(acraReportId, IssueDescriptionUtilsV1.parseDate(acraUserCrashDate));
                } catch (final ParseException e) {
                    throw new IssueParseException("Unable to parse user crash date of ACRA report "
                            + acraReportId, e);
                }
            }
        } else {
            throw new IssueParseException("No crash occurrence table found in the description");
        }

        if (m.find()) {
            throw new IssueParseException("More than 1 occurrence table found in the description");
        }

        if (MapUtils.isEmpty(occur)) {
            throw new IssueParseException("0 user crash occurrence found in the description");
        }

        return occur;
    }

    /**
     * Extracts the bug stacktrace from the description.
     * 
     * @param pDescription
     *            the issue description
     * @param pStacktraceMD5
     *            the stacktrace MD5 hash the issue is related to
     * @return the stacktrace
     * @throws IssueParseException
     *             malformed issue description
     */
    private String parseStacktrace(final String pDescription, final String pStacktraceMD5)
            throws IssueParseException {

        String stacktrace = null;

        // escape braces { and } to use strings in regexp
        final String start = "<pre class=\"javastacktrace\">";
        final String qStart = Pattern.quote(start);
        final String end = "</pre>";
        final String qEnd = Pattern.quote(end);

        final Pattern p = Pattern.compile(qStart + "(.*)" + qEnd, Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(pDescription);

        if (m.find()) {
            stacktrace = m.group(1);
            // if a start tag or an end tag is found in the stacktrace, then there is a problem
            if (StringUtils.contains(stacktrace, start) || StringUtils.contains(stacktrace, end)) {
                throw new IssueParseException("Invalid stacktrace block");
            }
        } else {
            throw new IssueParseException("0 stacktrace block found in the description");
        }

        return stacktrace;
    }

    /**
     * Gets the stacktrace.
     * 
     * @return the stacktrace
     */
    public String getStacktrace() {

        return stacktrace;
    }

    /**
     * Gets the stacktraceMD5.
     * 
     * @return the stacktraceMD5
     */
    public String getStacktraceMD5() {

        return stacktraceMD5;
    }

    /**
     * Gets the occurrences.
     * 
     * @return the occurrences
     */
    public Map<String, Date> getOccurrences() {

        return occurrences;
    }

}
