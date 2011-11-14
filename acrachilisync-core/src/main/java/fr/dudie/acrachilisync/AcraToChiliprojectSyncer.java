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

package fr.dudie.acrachilisync;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.redmine.ta.AuthenticationException;
import org.redmine.ta.NotFoundException;
import org.redmine.ta.RedmineException;
import org.redmine.ta.RedmineManager;
import org.redmine.ta.beans.Issue;
import org.redmine.ta.beans.IssueRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;

import fr.dudie.acrachilisync.exception.SynchronizationException;
import fr.dudie.acrachilisync.handler.AcraReportHandler;
import fr.dudie.acrachilisync.handler.AcraToChiliprojectSyncHandler;
import fr.dudie.acrachilisync.model.EditableAcraReport;
import fr.dudie.acrachilisync.model.SyncStatus;
import fr.dudie.acrachilisync.utils.ChiliprojectUtils;
import fr.dudie.acrachilisync.utils.ConfigurationManager;
import fr.dudie.acrachilisync.utils.CreationDateIssueComparator;

/**
 * Main service to executes a synchronization.
 * 
 * @author Jérémie Huchet
 */
public final class AcraToChiliprojectSyncer {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AcraToChiliprojectSyncer.class);

    /** The application name used during Google authentication. */
    private static final String APP_NAME = "dudie-acrachilisync-0.1";

    /**
     * A format string to build a worksheet list feed URL. You must provide two string arguments to
     * format it:
     * <ol>
     * <li>the document key</li>
     * <li>the worksheet id</li>
     * </ol>
     */
    public static final String WORKSHEET_URL_FORMAT = "https://spreadsheets.google.com"
            + "/feeds/list/%s/%s/private/full?sq=stacktracemd5%%3D%%3D%%22%%22";

    private final ConfigurationManager config;

    /** The Chiliproject client. */
    private final RedmineManager redmineClient;

    /** The Spreadsheet client. */
    private final SpreadsheetService client;

    /** The URL to access the worksheet containing Acra reports. */
    private final URL listFeedUrl;

    /** The list of report handler to invoke during synchronization. */
    private final List<AcraReportHandler> reportHandlers;

    /**
     * Constructor.
     * 
     * @param pConfiguration
     *            the configuration
     * @throws com.google.gdata.util.AuthenticationException
     *             invalid Google credentials
     */
    public AcraToChiliprojectSyncer(final Configuration pConfiguration)
            throws com.google.gdata.util.AuthenticationException {

        config = ConfigurationManager.getInstance(pConfiguration);

        redmineClient = new RedmineManager(config.CHILIPROJECT_HOST.toString(),
                config.CHILIPROJECT_API_KEY);

        client = new SpreadsheetService(APP_NAME);
        client.setUserCredentials(config.GOOGLE_LOGIN, config.GOOGLE_PASSWORD);

        listFeedUrl = config.SPREADSHEET_FEED_URL;

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("spreadsheet list feed URL: {}", listFeedUrl);
        }

        reportHandlers = new ArrayList<AcraReportHandler>();
        reportHandlers.add(new AcraToChiliprojectSyncHandler(redmineClient));
    }

    /**
     * Adds a report handler.
     * 
     * @param pHandler
     *            the report handler to add
     */
    public void addReportHandler(final AcraReportHandler pHandler) {

        reportHandlers.add(pHandler);
    }

    /**
     * Removes a report handler.
     * 
     * @param pHandler
     *            the report handler to remove
     */
    public void removeReportHandler(final AcraReportHandler pHandler) {

        reportHandlers.remove(pHandler);
    }

    /**
     * Gets unsynchronized Acra reports from the Google spreadsheet.
     * 
     * @return the list of rows of the Acra reports spreadsheet not already synchronized
     * @throws IOException
     * @throws ServiceException
     */
    private List<EditableAcraReport> retrieveUnsyncedElements() throws IOException,
            ServiceException {

        final ListFeed listFeed = client.getFeed(listFeedUrl, ListFeed.class);
        final List<EditableAcraReport> reports = new ArrayList<EditableAcraReport>();
        for (final ListEntry listEntry : listFeed.getEntries()) {
            reports.add(new EditableAcraReport(listEntry));
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("found {} reports to sync", CollectionUtils.size(reports));
        }
        return reports;
    }

    /**
     * Starts the synchronization between Acra reports Google spreadsheet and Chiliproject
     * bugtracker.
     * 
     * @throws IOException
     * @throws ServiceException
     * @throws RedmineException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws ParseException
     */
    public void startSynchronization() throws IOException, ServiceException,
            AuthenticationException, NotFoundException, RedmineException, ParseException {

        // retrieve new issues
        final List<EditableAcraReport> listReports = retrieveUnsyncedElements();

        // update Chiliproject
        for (final EditableAcraReport report : listReports) {

            final Issue issue = getIssueForStack(report.getStacktraceMD5());

            try {
                if (null == issue) {
                    LOGGER.debug("Got a new bugreport: reportId={}", report.getId());
                    for (final AcraReportHandler handler : reportHandlers) {
                        handler.onNewReport(report);
                    }
                } else if (ChiliprojectUtils.isSynchronized(report, issue)) {
                    LOGGER.debug("Got a bugreport already synchronized: reportId={}",
                            report.getId());
                    for (final AcraReportHandler handler : reportHandlers) {
                        handler.onKnownIssueAlreadySynchronized(report, issue);
                    }
                } else {
                    LOGGER.debug(
                            "Got a new bugreport with a stacktrace similar to an existing ticket: reportId={}",
                            report.getId());
                    for (final AcraReportHandler handler : reportHandlers) {
                        handler.onKnownIssueNotSynchronized(report, issue);
                    }
                }
            } catch (final SynchronizationException e) {
                report.mergeSyncStatus(SyncStatus.FAILURE);
                LOGGER.error("Unable to synchronize ACRA report " + report.getId(), e);
            }
        }

        try {
            for (final AcraReportHandler handler : reportHandlers) {
                handler.onFinishReceivingNewReports();
            }
        } catch (final SynchronizationException e) {
            for (final EditableAcraReport report : listReports) {
                report.mergeSyncStatus(SyncStatus.FAILURE);
            }
            LOGGER.error("Unable to finalize ACRA report synchronization");
        }

        // update/set stack_trace_md5 cells
        for (final EditableAcraReport report : listReports) {
            if (SyncStatus.SUCCESS.equals(report.getStatus())) {
                report.commitStacktraceMD5();
            }
        }
    }

    /**
     * Search for the issue related to the given MD5 stacktrace hash.
     * 
     * @param pStacktraceMD5
     *            an MD5 stacktrace hash
     * @return the Chiliproject issue related to the given stacktrace or null if no issue found
     * @throws IOException
     * @throws AuthenticationException
     * @throws NotFoundException
     * @throws RedmineException
     */
    private Issue getIssueForStack(final String pStacktraceMD5) throws IOException,
            AuthenticationException, NotFoundException, RedmineException {

        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("project_id", String.valueOf(config.CHILIPROJECT_PROJECT_ID));
        parameters.put(String.format("cf_%d", config.CHILIPROJECT_STACKTRACE_MD5_CF_ID),
                pStacktraceMD5);
        final List<Issue> results = redmineClient.getIssues(parameters);
        Issue issue = null;
        if (CollectionUtils.size(results) > 1) {
            issue = handleMultipleIssuesForSameStacktrace(results);
        } else if (CollectionUtils.size(results) == 1) {
            issue = results.get(0);
        }
        return issue;
    }

    /**
     * Invoked when detecting a duplicate issue (two issues with the same
     * {@link ConfigurationManager#CHILIPROJECT_STACKTRACE_MD5_CF_ID} customfield value.
     * <p>
     * The oldest issue is kept, the others are closed.
     * 
     * @param pDuplicateIssues
     *            the list of duplicate issues
     * @return the kept issue
     * @throws RedmineException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws IOException
     */
    private Issue handleMultipleIssuesForSameStacktrace(final List<Issue> pDuplicateIssues)
            throws IOException, AuthenticationException, NotFoundException, RedmineException {

        Collections.sort(pDuplicateIssues, new CreationDateIssueComparator());

        final Issue originalIssue = pDuplicateIssues.get(0);

        final List<Issue> issuesToClose = pDuplicateIssues.subList(1, pDuplicateIssues.size());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Closing issues {} duplicates of issue id={}",
                    Arrays.toString(issuesToClose.toArray()), originalIssue.getId());
        }
        for (final Issue issue : issuesToClose) {

            // add duplicate relation;
            final IssueRelation duplicate = new IssueRelation();
            duplicate.setIssueId(originalIssue.getId());
            duplicate.setType(config.CHILIPROJECT_RELATION_DUPLICATE_NAME);
            issue.getRelations().add(duplicate);

            // close issue
            issue.setStatusId(config.CHILIPROJECT_STATUS_CLOSED_ID);

            redmineClient.updateIssue(issue);
        }

        return originalIssue;
    }
}
