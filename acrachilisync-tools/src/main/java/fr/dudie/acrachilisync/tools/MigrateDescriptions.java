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

package fr.dudie.acrachilisync.tools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.redmine.ta.RedmineManager;
import org.redmine.ta.beans.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import fr.dudie.acrachilisync.exception.MalformedSpreadsheetLineException;
import fr.dudie.acrachilisync.exception.SynchronizationException;
import fr.dudie.acrachilisync.model.AcraReport;
import fr.dudie.acrachilisync.model.ErrorOccurrence;
import fr.dudie.acrachilisync.tools.upgrade.DescriptionUpgradeException;
import fr.dudie.acrachilisync.tools.upgrade.IssueDescriptionReaderV1;
import fr.dudie.acrachilisync.utils.ConfigurationManager;
import fr.dudie.acrachilisync.utils.IssueDescriptionBuilder;
import fr.dudie.acrachilisync.utils.IssueDescriptionUtils;

/**
 * @author Jérémie Huchet
 */
public class MigrateDescriptions {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateDescriptions.class);

    /** The application name used during Google authentication. */
    private static final String APP_NAME = "dudie-acrachilisync-0.2";

    /**
     * A format string to build a worksheet list feed URL. You must provide two string arguments to
     * format it:
     * <ol>
     * <li>the document key</li>
     * <li>the worksheet id</li>
     * <li>the md5stacktrace</li>
     * </ol>
     */
    private static final String WORKSHEET_URL_FORMAT = "https://spreadsheets.google.com"
            + "/feeds/list/%s/%s/private/full?sq=reportid%%3D%%3D%%22%s%%22";

    private final Configuration config;

    /** The Chiliproject client. */
    private final RedmineManager redmine;

    /** The Spreadsheet client. */
    private final SpreadsheetService googleDoc;

    /**
     * Constructor.
     * 
     * @param pConfiguration
     *            the configuration
     * @throws com.google.gdata.util.AuthenticationException
     *             invalid Google credentials
     */
    public MigrateDescriptions(final Configuration pConfiguration) throws AuthenticationException {

        config = pConfiguration;
        // load configuration
        final ConfigurationManager confManager = ConfigurationManager.getInstance(pConfiguration);

        redmine = new RedmineManager(confManager.CHILIPROJECT_HOST.toString(),
                confManager.CHILIPROJECT_API_KEY);

        googleDoc = new SpreadsheetService(APP_NAME);
        googleDoc.setUserCredentials(confManager.GOOGLE_LOGIN, confManager.GOOGLE_PASSWORD);
    }

    public static void main(final String[] args) throws AuthenticationException,
            ConfigurationException, SynchronizationException {

        final MigrateDescriptions updater = new MigrateDescriptions(new PropertiesConfiguration(
                "acrachilisync.properties"));

        updater.upgrade(1, 2);
    }

    private void upgrade(final int pOldVersion, final int pNewVersion)
            throws SynchronizationException {

        final int project = ConfigurationManager.getInstance().CHILIPROJECT_PROJECT_ID;
        final int tracker = ConfigurationManager.getInstance().CHILIPROJECT_TRACKER_ID;

        final Map<String, String> selection = new HashMap<String, String>();
        selection.put("project_id", String.valueOf(project));
        selection.put("tracker_id", String.valueOf(tracker));
        selection.put("status_id", "*");
        try {
            final List<Issue> issuesToUpgrade = redmine.getIssues(selection);
            LOGGER.info("Starting upgrade from version {} to {} on {} issues", new Object[] {
                    pOldVersion, pNewVersion, CollectionUtils.size(issuesToUpgrade) });

            for (final Issue issue : issuesToUpgrade) {
                upgrade(pOldVersion, pNewVersion, issue);
            }
        } catch (final Exception e) {
            throw new SynchronizationException("can't retrive issues from redmine server", e);
        }
    }

    public void upgrade(final int pOldVersion, final int pNewVersion, final Issue pIssue) {

        if (pOldVersion == 1 && pNewVersion == 2) {
            LOGGER.info("Issue #{}: upgrade needed from version {} to {}",
                    new Object[] { pIssue.getId(), pOldVersion, pNewVersion });
            try {
                upgradeFrom1To2(pIssue);
            } catch (final DescriptionUpgradeException e) {
                LOGGER.error("Can't migrate issue #" + pIssue.getId(), e);
            }
        } else {
            LOGGER.info("Issue #{}: current version is {}, no upgrade needed", pIssue.getId(),
                    pOldVersion);
        }
    }

    private void upgradeFrom1To2(final Issue pIssue) throws DescriptionUpgradeException {

        try {
            final IssueDescriptionReaderV1 reader = new IssueDescriptionReaderV1(pIssue);
            final Map<String, Date> occurrences = reader.getOccurrences();
            final List<ErrorOccurrence> errors = toErrorOccurrences(occurrences.keySet());
            final IssueDescriptionBuilder builder = new IssueDescriptionBuilder(
                    reader.getStacktrace());
            builder.setOccurrences(errors);
            pIssue.setDescription(builder.build());
            // redmine.updateIssue(pIssue);
        } catch (final Exception e) {
            throw new DescriptionUpgradeException(pIssue, e);
        }
    }

    private List<ErrorOccurrence> toErrorOccurrences(final Set<String> md5stacktraces)
            throws MalformedURLException, IOException, ServiceException,
            MalformedSpreadsheetLineException {

        final List<ErrorOccurrence> errors = new ArrayList<ErrorOccurrence>();

        for (final String md5stack : md5stacktraces) {
            // retrieve ACRA report line
            final String listFeedUrl = String.format(WORKSHEET_URL_FORMAT,
                    config.getString("google.spreadsheet.document.key"),
                    config.getString("google.spreadsheet.worksheet.id"), md5stack);
            LOGGER.debug("retrieving {}", listFeedUrl);
            final ListFeed listFeed = googleDoc.getFeed(new URL(listFeedUrl), ListFeed.class);
            final ListEntry entry = listFeed.getEntries().iterator().next();

            final AcraReport report = new AcraReport(entry.getCustomElements());
            errors.add(IssueDescriptionUtils.toErrorOccurrence(report));
        }

        return errors;
    }
}
