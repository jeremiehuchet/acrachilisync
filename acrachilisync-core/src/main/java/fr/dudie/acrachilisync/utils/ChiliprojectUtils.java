package fr.dudie.acrachilisync.utils;

import org.redmine.ta.beans.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.exception.IssueParseException;
import fr.dudie.acrachilisync.model.AcraReport;
import fr.dudie.acrachilisync.model.AcraReportHeader;

/**
 * @author Jérémie Huchet
 */
public final class ChiliprojectUtils {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChiliprojectUtils.class);

    /**
     * Private constructor to avoid insantiation.
     */
    private ChiliprojectUtils() {

    }

    /**
     * Gets wheter or not the given report has already been synchronized with the given issue.
     * 
     * @param pReport
     *            an ACRA report
     * @param pIssue
     *            a Chiliproject issue
     * @return true if the given report has already been synchronized with the given issue
     * @throws IssueParseException
     *             the Date of one of the synchronized issue is unreadable
     */
    public static boolean isSynchronized(final AcraReport pReport, final Issue pIssue)
            throws IssueParseException {

        final IssueDescriptionReader reader = new IssueDescriptionReader(pIssue);
        return reader.getOccurrences().containsKey(pReport.getValue(AcraReportHeader.REPORT_ID));
    }
}
