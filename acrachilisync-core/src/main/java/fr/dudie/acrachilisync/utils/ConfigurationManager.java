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

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.AcraToChiliprojectSyncer;

/**
 * Loads some configuration elements from a file.
 * <p>
 * Look for a {@value #CONF_FILE_NAME} file located at:
 * <ul>
 * <li>the root of the current directory</li>
 * <li>the root of the classpath</li>
 * </ul>
 * 
 * @author Jérémie Huchet
 */
public final class ConfigurationManager {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManager.class);

    /** The Redmine server host name. */
    public final URL CHILIPROJECT_HOST;

    /** The Redmine server API key. */
    public final String CHILIPROJECT_API_KEY;

    /** The Redmine project identifier where issues must be pushed. */
    public final int CHILIPROJECT_PROJECT_ID;

    /** The Redmine tracker identifier where issues must be pushed. */
    public final int CHILIPROJECT_TRACKER_ID;

    /** The Redmine custom field identifier containing the MD5 hash to identify stacktraces. */
    public final int CHILIPROJECT_STACKTRACE_MD5_CF_ID;

    /** The Chiliproject <i>status closed</i> identifier. */
    public final Integer CHILIPROJECT_STATUS_CLOSED_ID;

    /** The Chiliproject <i>duplicate relationship</i> name. */
    public final String CHILIPROJECT_RELATION_DUPLICATE_NAME;

    /** The feed URL of the Google spreadsheet worksheet containing Acra reports. */
    public final URL SPREADSHEET_FEED_URL;

    /** The Google account login to use. */
    public final String GOOGLE_LOGIN;

    /** The Google account password. */
    public final String GOOGLE_PASSWORD;

    /** The unique instance of the configuration manage. */
    private static ConfigurationManager instance;

    /**
     * Constructor. Load the configuration elements.
     */
    private ConfigurationManager(final Configuration pConf) {

        CHILIPROJECT_HOST = getURL(pConf.getString("chiliproject.host"));
        CHILIPROJECT_API_KEY = pConf.getString("chiliproject.api.key");
        CHILIPROJECT_PROJECT_ID = pConf.getInt("chiliproject.project.id");
        CHILIPROJECT_TRACKER_ID = pConf.getInt("chiliproject.tracker.id");
        CHILIPROJECT_STACKTRACE_MD5_CF_ID = pConf
                .getInt("chiliproject.stacktraceMD5CustomField.id");
        CHILIPROJECT_STATUS_CLOSED_ID = pConf.getInt("chiliproject.issue.status.closed.id");
        CHILIPROJECT_RELATION_DUPLICATE_NAME = pConf
                .getString("chiliproject.issue.relation.duplicate.name");

        final String documentKey = pConf.getString("google.spreadsheet.document.key");
        final String worksheetId = pConf.getString("google.spreadsheet.worksheet.id");

        final String url = String.format(AcraToChiliprojectSyncer.WORKSHEET_URL_FORMAT,
                documentKey, worksheetId);
        SPREADSHEET_FEED_URL = getURL(url);

        GOOGLE_LOGIN = pConf.getString("google.account.username");
        GOOGLE_PASSWORD = pConf.getString("google.account.password");

    }

    /**
     * Converts a String representing an URL to an {@link URL} or throw a
     * {@link org.apache.commons.configuration.ConversionException}.
     * 
     * @param pUrl
     *            a URL as a string
     * @return an URL object
     */
    public URL getURL(final String pUrl) {

        try {
            return new URL(pUrl);
        } catch (final MalformedURLException e) {
            throw new ConversionException("Cannot parse URL from string " + pUrl, e);
        }
    }

    /**
     * Gets the current configuration.
     * 
     * @return the current configuration
     * @throws IllegalStateException
     *             if there no configuration currently loaded
     */
    public static ConfigurationManager getInstance() throws IllegalStateException {

        if (null == instance) {
            throw new IllegalStateException("Configuration hasn't been loaded");
        }
        return instance;
    }

    /**
     * Loads a new configuration (the given one replace the old one).
     * 
     * @return the current configuration
     */
    public static ConfigurationManager getInstance(final Configuration pConfiguration) {

        instance = new ConfigurationManager(pConfiguration);
        return instance;
    }

}
