package fr.dudie.acrachilisync.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.data.spreadsheet.CustomElementCollection;

import fr.dudie.acrachilisync.model.AcraReport;
import fr.dudie.acrachilisync.model.AcraReportHeader;

/**
 * Test {@link AcraReport} instantiation with empty fields and malformed values.
 * 
 * @author Jérémie Huchet
 */
@RunWith(Parameterized.class)
public class AcraReportTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AcraReportTest.class);

    @Parameters
    public static List<Object[]> data() {

        final List<Object[]> params = new ArrayList<Object[]>();
        // test not mandatory fields against the empty field
        for (final AcraReportHeader header : AcraReportHeader.values()) {
            if (!header.isMandatory()) {
                params.add(new Object[] { header, "" });
            }
        }
        return params;
    }

    /** A well-formed report elements collection like the gdata-apis should return. */
    private CustomElementCollection reportElements;

    /** The {@link AcraReportHeader} to edit. */
    private final AcraReportHeader header;

    /** The value to set for the test. */
    private final String value;

    /**
     * Set up a test.
     * 
     * @param pHeader
     *            the {@link AcraReportHeader} to edit
     * @param pHeaderValue
     *            the value to set for the test
     */
    public AcraReportTest(final AcraReportHeader pHeader, final String pHeaderValue) {

        this.header = pHeader;
        this.value = pHeaderValue;
    }

    /**
     * Sets up a well-formed report elements collection.
     */
    @Before
    public void setup() {

        reportElements = new CustomElementCollection();
        reportElements.setValueLocal(AcraReportHeader.ANDROID_VERSION.tagName(), "1.6");
        reportElements.setValueLocal(AcraReportHeader.APP_VERSION_CODE.tagName(), "16");
        reportElements.setValueLocal(AcraReportHeader.APP_VERSION_NAME.tagName(), "app verion");
        reportElements.setValueLocal(AcraReportHeader.AVAILABLE_MEM_SIZE.tagName(), "48398446");
        reportElements.setValueLocal(AcraReportHeader.BRAND.tagName(), "generic");
        reportElements.setValueLocal(AcraReportHeader.BUILD.tagName(), "BOARD=unknown");
        reportElements.setValueLocal(AcraReportHeader.CRASH_CONFIGURATION.tagName(),
                "keyboard=KEYBOARD_QWERTY");
        reportElements.setValueLocal(AcraReportHeader.CUSTOM_DATA.tagName(), "key=value");
        reportElements.setValueLocal(AcraReportHeader.DEVICE_FEATURES.tagName(),
                "Data available only with API Level > 5");
        reportElements.setValueLocal(AcraReportHeader.DEVICE_ID.tagName(), "azerty");
        reportElements.setValueLocal(AcraReportHeader.DISPLAY.tagName(), "width=240");
        reportElements.setValueLocal(AcraReportHeader.DROPBOX.tagName(), "none");
        reportElements.setValueLocal(AcraReportHeader.DUMPSYS_MEMINFO.tagName(),
                "currently running services:");
        reportElements.setValueLocal(AcraReportHeader.ENVIRONMENT.tagName(),
                "getDataDirectory=/data");
        reportElements.setValueLocal(AcraReportHeader.EVENTSLOG.tagName(), "none");
        reportElements.setValueLocal(AcraReportHeader.FILE_PATH.tagName(),
                "/data/data/com.vendor/files");
        reportElements.setValueLocal(AcraReportHeader.INITIAL_CONFIGURATION.tagName(),
                "locale=en_US");
        reportElements.setValueLocal(AcraReportHeader.INSTALLATION_ID.tagName(),
                "034e18a7-f081-45a6-a298-b5c679d97e68");
        reportElements.setValueLocal(AcraReportHeader.IS_SILENT.tagName(), "true");
        reportElements.setValueLocal(AcraReportHeader.LOGCAT.tagName(), "none");
        reportElements.setValueLocal(AcraReportHeader.PACKAGE_NAME.tagName(), "com.vendor");
        reportElements.setValueLocal(AcraReportHeader.PHONE_MODEL.tagName(), "sdk");
        reportElements.setValueLocal(AcraReportHeader.PRODUCT.tagName(), "sdk");
        reportElements.setValueLocal(AcraReportHeader.RADIOLOG.tagName(), "none");
        reportElements.setValueLocal(AcraReportHeader.REPORT_ID.tagName(),
                "d0eabc31-f7aa-440b-b000-4001dda3db40");
        reportElements.setValueLocal(AcraReportHeader.SETTINGS_SECURE.tagName(), "ADB_ENABLED=1");
        reportElements.setValueLocal(AcraReportHeader.SETTINGS_SYSTEM.tagName(),
                "ACCELEROMETER_ROTATION=1");
        reportElements.setValueLocal(AcraReportHeader.SHARED_PREFERENCES.tagName(), "default");
        reportElements.setValueLocal(AcraReportHeader.STACK_TRACE.tagName(),
                "java.lang.IllegalArgumentException");
        reportElements.setValueLocal(AcraReportHeader.STACK_TRACE_MD5.tagName(),
                "73ad687b153f570e218c5b5c2226edc4");
        reportElements.setValueLocal(AcraReportHeader.TOTAL_MEM_SIZE.tagName(), "71099964");
        reportElements.setValueLocal(AcraReportHeader.USER_APP_START_DATE.tagName(),
                "2010-07-12T22:10:40.000+02:00");
        reportElements.setValueLocal(AcraReportHeader.USER_CRASH_DATE.tagName(),
                "2011-05-11T20:42:40.000+02:00");
        reportElements.setValueLocal(AcraReportHeader.USER_EMAIL.tagName(), "N/A");

        if (AcraReportHeader.values().length != reportElements.getTags().size()) {
            throw new IllegalStateException("Bad dataset: the sample report elements is invalid");
        }

    }

    /**
     * Tries to instantiates an AcraReport with the modified header value.
     */
    @Test
    public void testReportInstantiation() {

        reportElements.setValueLocal(header.tagName(), value);
        try {
            new AcraReport(reportElements);
        } catch (final Throwable t) {
            final String msg = String.format("AcraReport instantiation failed with %s='%s'",
                    header, value);
            LOGGER.error(msg, t);
            fail(msg);
        }
    }
}
