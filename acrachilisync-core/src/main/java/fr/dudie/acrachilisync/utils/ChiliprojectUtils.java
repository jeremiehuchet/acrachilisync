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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.redmine.ta.beans.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.exception.IssueParseException;
import fr.dudie.acrachilisync.model.AcraReport;

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
        return CollectionUtils.exists(reader.getOccurrences(), new ReportPredicate(pReport));
    }

    /**
     * A predicate to find two reports with the same identifier.
     * 
     * @see AcraReport#getId()
     * @author Jérémie Huchet
     */
    private static class ReportPredicate implements Predicate {

        private final AcraReport report;

        public ReportPredicate(final AcraReport pReport) {

            this.report = pReport;
        }

        @Override
        public boolean evaluate(final Object object) {

            if (object instanceof AcraReport) {
                return ((AcraReport) object).getId().equals(this.report.getId());
            } else {
                return false;
            }
        }

    }
}
