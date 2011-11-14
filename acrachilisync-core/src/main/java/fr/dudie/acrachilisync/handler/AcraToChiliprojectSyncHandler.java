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

package fr.dudie.acrachilisync.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.redmine.ta.RedmineManager;
import org.redmine.ta.beans.CustomField;
import org.redmine.ta.beans.Issue;
import org.redmine.ta.beans.Project;
import org.redmine.ta.beans.Tracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.exception.IssueParseException;
import fr.dudie.acrachilisync.exception.SynchronizationException;
import fr.dudie.acrachilisync.model.AcraReport;
import fr.dudie.acrachilisync.model.AcraReportHeader;
import fr.dudie.acrachilisync.model.SyncStatus;
import fr.dudie.acrachilisync.utils.ConfigurationManager;
import fr.dudie.acrachilisync.utils.IssueDescriptionBuilder;
import fr.dudie.acrachilisync.utils.IssueDescriptionReader;

/**
 * The first {@link AcraReportHandler} triggered: synchronizes Acra reports to the Chiliproject
 * server.
 * 
 * @author Jérémie Huchet
 */
public final class AcraToChiliprojectSyncHandler implements AcraReportHandler {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AcraToChiliprojectSyncHandler.class);

    /** The Chiliproject client. */
    private final RedmineManager redmineClient;

    /**
     * Constructor.
     * 
     * @param pRedmineClient
     *            the Chiliproject client
     */
    public AcraToChiliprojectSyncHandler(final RedmineManager pRedmineClient) {

        redmineClient = pRedmineClient;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.dudie.acrachilisync.handler.AcraReportHandler#onNewReport(fr.dudie.acrachilisync.model.AcraReport)
     */
    @Override
    public void onNewReport(final AcraReport pReport) throws SynchronizationException {

        final Issue issue = new Issue();

        final Project project = new Project();
        project.setId(ConfigurationManager.getInstance().CHILIPROJECT_PROJECT_ID);
        issue.setProject(project);

        final Tracker tracker = new Tracker();
        tracker.setId(ConfigurationManager.getInstance().CHILIPROJECT_TRACKER_ID);
        issue.setTracker(tracker);

        final CustomField md5CustomField = new CustomField();
        md5CustomField.setId(ConfigurationManager.getInstance().CHILIPROJECT_STACKTRACE_MD5_CF_ID);
        md5CustomField.setValue(pReport.getStacktraceMD5());
        issue.getCustomFields().add(md5CustomField);

        final String stack = pReport.getValue(AcraReportHeader.STACK_TRACE);
        final IssueDescriptionBuilder description = new IssueDescriptionBuilder(stack);
        description.addOccurrence(pReport.getId(), pReport.getUserCrashDate());

        final Matcher m = Pattern.compile("(.*)$", Pattern.MULTILINE).matcher(stack);
        m.find();
        issue.setSubject(m.group());
        issue.setDescription(description.build());

        try {
            redmineClient.createIssue(
                    String.valueOf(ConfigurationManager.getInstance().CHILIPROJECT_PROJECT_ID),
                    issue);
        } catch (final Exception e) {
            pReport.setStatus(SyncStatus.FAILURE);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Failure while creating issue: \n{}",
                        ToStringBuilder.reflectionToString(issue, ToStringStyle.MULTI_LINE_STYLE));
            }
            throw new SynchronizationException("Unable to create issue for ACRA report: "
                    + pReport.getId(), e);
        }
        pReport.setStatus(SyncStatus.SUCCESS);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.dudie.acrachilisync.handler.AcraReportHandler#onKnownIssueAlreadySynchronized(fr.dudie.acrachilisync.model.AcraReport,
     *      org.redmine.ta.beans.Issue)
     */
    @Override
    public void onKnownIssueAlreadySynchronized(final AcraReport pReport, final Issue pIssue)
            throws SynchronizationException {

        pReport.setStatus(SyncStatus.SUCCESS);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.dudie.acrachilisync.handler.AcraReportHandler#onKnownIssueNotSynchronized(fr.dudie.acrachilisync.model.AcraReport,
     *      org.redmine.ta.beans.Issue)
     */
    @Override
    public void onKnownIssueNotSynchronized(final AcraReport pReport, final Issue pIssue)
            throws SynchronizationException {

        IssueDescriptionReader reader = null;
        try {
            reader = new IssueDescriptionReader(pIssue);

        } catch (final IssueParseException e) {
            throw new SynchronizationException("Unable to parse description of issue "
                    + pIssue.getId(), e);
        }

        final IssueDescriptionBuilder builder = new IssueDescriptionBuilder(reader.getStacktrace());
        builder.setOccurrences(reader.getOccurrences());
        builder.addOccurrence(pReport.getId(), pReport.getUserCrashDate());

        pIssue.setDescription(builder.build());

        try {
            redmineClient.updateIssue(pIssue);
        } catch (final Exception e) {
            pReport.setStatus(SyncStatus.FAILURE);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Failure while updating issue: \n{}",
                        ToStringBuilder.reflectionToString(pIssue, ToStringStyle.MULTI_LINE_STYLE));
            }
            throw new SynchronizationException(
                    String.format("Unable to update issue %s for ACRA report %s", pIssue.getId(),
                            pReport.getId()), e);
        }
        pReport.setStatus(SyncStatus.SUCCESS);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.dudie.acrachilisync.handler.AcraReportHandler#onFinishReceivingNewReports()
     */
    @Override
    public void onFinishReceivingNewReports() throws SynchronizationException {

        // TJHU Auto-generated method stub

    }
}
