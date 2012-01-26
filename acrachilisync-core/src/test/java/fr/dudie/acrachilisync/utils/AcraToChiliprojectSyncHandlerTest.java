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

import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redmine.ta.RedmineManager;
import org.redmine.ta.beans.CustomField;
import org.redmine.ta.beans.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.data.spreadsheet.CustomElementCollection;

import fr.dudie.acrachilisync.exception.MalformedSpreadsheetLineException;
import fr.dudie.acrachilisync.exception.SynchronizationException;
import fr.dudie.acrachilisync.handler.AcraToChiliprojectSyncHandler;
import fr.dudie.acrachilisync.model.AcraReport;
import fr.dudie.acrachilisync.model.AcraReportHeader;
import fr.dudie.acrachilisync.model.ErrorOccurrence;
import fr.dudie.acrachilisync.model.SyncStatus;

public final class AcraToChiliprojectSyncHandlerTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AcraToChiliprojectSyncHandlerTest.class);

    private RedmineManager redmineClient;

    private AcraToChiliprojectSyncHandler handler;

    private String stacktrace, stacktraceMD5, issueDescription;

    private List<ErrorOccurrence> occurrences;

    @BeforeClass
    public static void setup() throws ConfigurationException {

        final Configuration config = new PropertiesConfiguration("acrachilisync.properties");
        ConfigurationManager.getInstance(config);
    }

    @Before
    public void beforeTest() {

        redmineClient = mock(RedmineManager.class);
        handler = new AcraToChiliprojectSyncHandler(redmineClient);

        final StringWriter out = new StringWriter();
        final PrintWriter pw = new PrintWriter(out);
        new Exception("test error " + new Date()).printStackTrace(pw);
        stacktrace = out.toString();
        stacktraceMD5 = MD5Utils.toMD5hash(stacktrace);

        occurrences = new ArrayList<ErrorOccurrence>();
        for (int i = 0; i < 3; i++) {
            final ErrorOccurrence error = new ErrorOccurrence();
            error.setReportId("rep" + i);
            error.setAndroidVersion("2." + i);
            error.setCrashDate(new Date(-1000 * i));
            error.setDevice("device " + i);
            error.setVersionCode("" + i);
            error.setVersionName("v" + i);
            occurrences.add(error);
        }

        final IssueDescriptionBuilder builder = new IssueDescriptionBuilder(stacktrace);
        builder.setOccurrences(occurrences);
        issueDescription = builder.build();
    }

    @Test
    public void testOnNewReportAlreadyKnown() throws SynchronizationException,
            MalformedSpreadsheetLineException {

        final CustomElementCollection elems = new CustomElementCollection();
        for (final AcraReportHeader h : AcraReportHeader.values()) {
            elems.setValueLocal(h.tagName(), h.name());
        }
        elems.setValueLocal(AcraReportHeader.REPORT_ID.tagName(), "reportID");
        elems.setValueLocal(AcraReportHeader.STACK_TRACE_MD5.tagName(), stacktraceMD5);
        elems.setValueLocal(AcraReportHeader.STACK_TRACE.tagName(), stacktrace);
        elems.setValueLocal(AcraReportHeader.USER_APP_START_DATE.tagName(),
                "2011-11-06T04:59:21.000+01:00");
        elems.setValueLocal(AcraReportHeader.USER_CRASH_DATE.tagName(),
                "2011-11-06T05:01:39.000+01:00");
        final AcraReport report = new AcraReport(elems);
        report.setStatus(SyncStatus.SUCCESS);

        final Issue issue = new Issue();
        issue.setId(111);
        issue.setDescription(issueDescription);
        issue.setCustomFields(new ArrayList<CustomField>());
        final CustomField cf = new CustomField();
        cf.setId(ConfigurationManager.getInstance().CHILIPROJECT_STACKTRACE_MD5_CF_ID);
        cf.setValue(stacktraceMD5);
        issue.getCustomFields().add(cf);

        handler.onKnownIssueNotSynchronized(report, issue);

        LOGGER.debug("New issue description:\n {}", ToStringBuilder.reflectionToString(issue));
    }
}
