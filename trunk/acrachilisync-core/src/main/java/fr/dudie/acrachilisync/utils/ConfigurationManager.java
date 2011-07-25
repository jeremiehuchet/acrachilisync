package fr.dudie.acrachilisync.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
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

    /** The configuration file name. */
    private static final String CONF_FILE_NAME = "acrachilisync.properties";

    static {
        try {
            InputStream in = null;
            File f = new File(CONF_FILE_NAME);
            if (!f.exists()) {
                final URL u = ConfigurationManager.class.getResource("/" + CONF_FILE_NAME);
                f = new File(u.toURI());
            }

            if (f.exists()) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Loading configuration from file {}", f.toURI().toString());
                }
                in = new FileInputStream(f);
            } else {
                LOGGER.error(
                        "Configuration file ({}) not found in current directory neither in classpath",
                        CONF_FILE_NAME);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Current directory: {}", System.getProperty("user.dir"));
                    LOGGER.debug("Classpath: {}", System.getProperty("java.class.path"));
                }
                throw new FileNotFoundException(CONF_FILE_NAME);
            }

            final Properties conf = new Properties();
            conf.load(in);
            CHILIPROJECT_HOST = new URL(getString(conf, "chiliproject.host"));
            CHILIPROJECT_API_KEY = getString(conf, "chiliproject.api.key");
            CHILIPROJECT_PROJECT_ID = getInteger(conf, "chiliproject.project.id");
            CHILIPROJECT_TRACKER_ID = getInteger(conf, "chiliproject.tracker.id");
            CHILIPROJECT_STACKTRACE_MD5_CF_ID = getInteger(conf,
                    "chiliproject.stacktraceMD5CustomField.id");
            CHILIPROJECT_STATUS_CLOSED_ID = getInteger(conf, "chiliproject.issue.status.closed.id");
            CHILIPROJECT_RELATION_DUPLICATE_NAME = getString(conf,
                    "chiliproject.issue.relation.duplicate.name");

            final String documentKey = getString(conf, "google.spreadsheet.document.key");
            final String worksheetId = getString(conf, "google.spreadsheet.worksheet.id");

            final String url = String.format(AcraToChiliprojectSyncer.WORKSHEET_URL_FORMAT,
                    documentKey, worksheetId);
            SPREADSHEET_FEED_URL = new URL(url);

            GOOGLE_LOGIN = getString(conf, "google.account.username");
            GOOGLE_PASSWORD = getString(conf, "google.account.password");
        } catch (final Throwable e) {
            throw new RuntimeException("Unable to load configuration", e);
        }
    }

    /**
     * Gets a property value as a String.
     * 
     * @param pConf
     *            the properties
     * @param pKey
     *            the property key
     * @return the string value
     */
    public static String getString(final Properties pConf, final String pKey) {

        return StringUtils.trim(pConf.getProperty(pKey));
    }

    /**
     * Gets a property value as an Integer.
     * 
     * @param pConf
     *            the properties
     * @param pKey
     *            the property key
     * @return the int value
     */
    public static int getInteger(final Properties pConf, final String pKey) {

        return Integer.parseInt(getString(pConf, pKey));
    }

    /** The Redmine server host name. */
    public static final URL CHILIPROJECT_HOST;

    /** The Redmine server API key. */
    public static final String CHILIPROJECT_API_KEY;

    /** The Redmine project identifier where issues must be pushed. */
    public static final int CHILIPROJECT_PROJECT_ID;

    /** The Redmine tracker identifier where issues must be pushed. */
    public static final int CHILIPROJECT_TRACKER_ID;

    /** The Redmine custom field identifier containing the MD5 hash to identify stacktraces. */
    public static final int CHILIPROJECT_STACKTRACE_MD5_CF_ID;

    /** The Chiliproject <i>status closed</i> identifier. */
    public static final Integer CHILIPROJECT_STATUS_CLOSED_ID;

    /** The Chiliproject <i>duplicate relationship</i> name. */
    public static final String CHILIPROJECT_RELATION_DUPLICATE_NAME;

    /** The feed URL of the Google spreadsheet worksheet containing Acra reports. */
    public static final URL SPREADSHEET_FEED_URL;

    /** The Google account login to use. */
    public static final String GOOGLE_LOGIN;

    /** The Google account password. */
    public static final String GOOGLE_PASSWORD;

    /**
     * Private constructor to avoid instantiation.
     */
    private ConfigurationManager() {

    }

}
