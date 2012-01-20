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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.redmine.ta.beans.CustomField;
import org.redmine.ta.beans.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.exception.IssueParseException;
import fr.dudie.acrachilisync.model.ErrorOccurrence;

/**
 * Helper class to extract informations from an issue description generated with
 * {@link IssueDescriptionBuilder}.
 * 
 * @author Jérémie Huchet
 * @see IssueDescriptionBuilder
 */
public final class IssueDescriptionReader {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDescriptionReader.class);

    /**
     * Regexp :
     * <code>|jsglq4354gjdslg4434|dd/MM/yyyy hh:mm:ss|2.3.3|15|0.3.1|Nexus One / google / passion|</code>
     * .
     */
    public static final String OCCURR_LINE_PATTERN = "\\|[\\w-]+\\|\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}\\|[^|]+\\|\\d+\\|[^|]+\\|[^|]+\\|";

    public static final Pattern DESCRIPTION_VERSION_PATTERN = Pattern

    .compile("\\s*%\\{visibility:hidden\\}description_version_(\\d+)%\\s*",
            Pattern.CASE_INSENSITIVE);

    /** The error stacktrace. */
    private final String stacktrace;

    /** The error stacktrace MD5 hash. */
    private final String stacktraceMD5;

    /** The occurrence time for each ACRA bug report. */
    private final List<ErrorOccurrence> occurrences = new ArrayList<ErrorOccurrence>();

    /**
     * Constructor.
     * <p>
     * The description must have been formatted with {@link IssueDescriptionBuilder} in order to
     * make this reader working.
     * 
     * @param pIssue
     *            the issue
     * @throws IssueParseException
     *             unable to parse the issue
     */
    public IssueDescriptionReader(final Issue pIssue) throws IssueParseException {

        checkDescriptionVersion(pIssue);

        stacktraceMD5 = getStacktraceMD5(pIssue);
        if (!StringUtils.isBlank(pIssue.getDescription())) {
            occurrences.addAll(parseAcraOccurrencesTable(pIssue.getDescription(), stacktraceMD5));
            stacktrace = parseStacktrace(pIssue.getDescription(), stacktraceMD5);
        } else {
            stacktrace = "";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Issue {} have an empty description", pIssue.getId());
            }
        }
    }

    /**
     * Checks the description version tag.
     * 
     * @param pIssue
     *            the issue
     * @throws IssueParseException
     *             the description version tag doesn't describe the expected version
     *             {@link IssueDescriptionUtils#DESCRIPTION_VERSION}
     */
    private void checkDescriptionVersion(final Issue pIssue) throws IssueParseException {

        final Matcher m = DESCRIPTION_VERSION_PATTERN.matcher(pIssue.getDescription());
        final int version;
        if (m.find()) {
            version = Integer.parseInt(m.group(1));
        } else {
            // default version is 1
            version = 1;
        }
        if (IssueDescriptionUtils.DESCRIPTION_VERSION != version) {
            throw new IssueParseException(String.format(
                    "Issue #%s has unsupported description: %d. Expected version %d ",
                    pIssue.getId(), version, IssueDescriptionUtils.DESCRIPTION_VERSION));
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
    private List<ErrorOccurrence> parseAcraOccurrencesTable(final String pDescription,
            final String pStacktraceMD5) throws IssueParseException {

        final List<ErrorOccurrence> occur = new ArrayList<ErrorOccurrence>();

        // escape braces { and } to use strings in regexp
        final String header = IssueDescriptionUtils.getOccurrencesTableHeader();
        final String escHeader = Pattern.quote(header);

        // regexp to find occurrences tables
        final Pattern p = Pattern.compile(escHeader + IssueDescriptionUtils.EOL + "(?:"
                + OCCURR_LINE_PATTERN + IssueDescriptionUtils.EOL + "+)+", Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE);
        final Matcher m = p.matcher(pDescription);

        if (m.find()) {
            // regexp to find occurrences lines
            final Pattern pLine = Pattern.compile(OCCURR_LINE_PATTERN);
            final Matcher mLine = pLine.matcher(m.group());
            while (mLine.find()) {
                try {
                    final StringTokenizer line = new StringTokenizer(mLine.group(), "|");
                    final String acraReportId = line.nextToken();
                    final String acraUserCrashDate = line.nextToken();
                    final String acraAndroidVersion = line.nextToken();
                    final String acraVersionCode = line.nextToken();
                    final String acraVersionName = line.nextToken();
                    final String acraDevice = line.nextToken();
                    final ErrorOccurrence error = new ErrorOccurrence();
                    error.setReportId(acraReportId);
                    try {
                        error.setCrashDate(IssueDescriptionUtils.parseDate(acraUserCrashDate));
                    } catch (final ParseException e) {
                        throw new IssueParseException(
                                "Unable to parse user crash date of ACRA report " + acraReportId, e);
                    }
                    error.setAndroidVersion(acraAndroidVersion);
                    error.setVersionCode(acraVersionCode);
                    error.setVersionName(acraVersionName);
                    error.setDevice(acraDevice);
                    occur.add(error);
                } catch (final NoSuchElementException e) {
                    throw new IssueParseException("Unable to parse ACRA report line: "
                            + mLine.group(), e);
                }
            }
        } else {
            throw new IssueParseException("No crash occurrence table found in the description");
        }

        if (m.find()) {
            throw new IssueParseException("More than 1 occurrence table found in the description");
        }

        if (CollectionUtils.isEmpty(occur)) {
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
    public List<ErrorOccurrence> getOccurrences() {

        return occurrences;
    }

}
