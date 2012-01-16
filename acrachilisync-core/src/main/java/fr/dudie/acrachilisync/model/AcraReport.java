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

package fr.dudie.acrachilisync.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.google.gdata.data.spreadsheet.CustomElementCollection;

import fr.dudie.acrachilisync.exception.MalformedSpreadsheetLineException;
import fr.dudie.acrachilisync.utils.MD5Utils;

/**
 * Wrapper class to access Acra Report field values.
 * 
 * @author Jérémie Huchet
 */
public class AcraReport {

    /** RFC 3339 date format. */
    private static final String RFC_339_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /** Google documents row values. */
    private final CustomElementCollection elements;

    /** The {@link AcraReportHeader#USER_CRASH_DATE} field value. */
    private final Date userCrashDate;

    /** The MD5 hash of the ACRA report tacktrace field. */
    private final String stacktraceMD5;

    /** Stores the synchronized states of this report entry. */
    private SyncStatus status = SyncStatus.NOT_STARTED;

    /**
     * Constructor.
     * <p>
     * Wraps the given spreadsheet list entry and provides read only access to report field values.
     * 
     * @param pCustomElements
     *            custom element collection of a Google Spreadsheet list entry
     * @throws MalformedSpreadsheetLineException
     *             the given ListEntry havn't all {@link AcraReportHeader} tag values, <br>
     *             unable to parse the crash date from the ACRA report
     */
    public AcraReport(final CustomElementCollection pCustomElements)
            throws MalformedSpreadsheetLineException {

        this.elements = pCustomElements;

        // check the list entry contains the
        for (final AcraReportHeader header : AcraReportHeader.values()) {
            if (!elements.getTags().contains(header.tagName()) || header.isMandatory()
                    && StringUtils.isEmpty(elements.getValue(header.tagName()))) {
                throw new MalformedSpreadsheetLineException(header, elements.getValue(header
                        .tagName()));
            }
        }

        final String date = getValue(AcraReportHeader.USER_CRASH_DATE);
        // weird manipulation to parse the date... remove ':' from the timezone
        // before: 2011-07-12T22:42:40.000+02:00
        // after: 2011-07-12T22:42:40.000+0200
        final StringBuilder _date = new StringBuilder();
        _date.append(date.substring(0, date.length() - 3));
        _date.append(date.substring(date.length() - 2));
        try {
            userCrashDate = new SimpleDateFormat(RFC_339_DATE_FORMAT).parse(_date.toString());
        } catch (final ParseException e) {
            throw new IllegalArgumentException(
                    "The given spreadsheet ListEntry usercrashdate field value is malformed", e);
        }

        stacktraceMD5 = MD5Utils
                .toMD5hash(StringUtils.trim(getValue(AcraReportHeader.STACK_TRACE)));
    }

    /**
     * Sets the new sync status if the current value is different from {@link SyncStatus#FAILURE}.
     * 
     * @param pStatus
     *            the new status to set
     */
    public final void mergeSyncStatus(final SyncStatus pStatus) {

        switch (status) {
        case FAILURE:
            break;

        default:
            status = pStatus;
            break;
        }
    }

    /*
     * Property access-methods.
     */

    /**
     * Gets the report value for the given field.
     * 
     * @param pHeader
     *            the field name
     * @return the field value
     */
    public final String getValue(final AcraReportHeader pHeader) {

        return elements.getValue(pHeader.tagName());
    }

    /**
     * Gets the status.
     * 
     * @return the status
     */
    public final SyncStatus getStatus() {

        return status;
    }

    /**
     * Sets the status.
     * 
     * @param pStatus
     *            the status to set
     */
    public final void setStatus(final SyncStatus pStatus) {

        this.status = pStatus;
    }

    /*
     * Easy access/shortcuts to some properties.
     */

    /**
     * Gets the {@link AcraReportHeader#REPORT_ID} field value.
     * 
     * @return the report identifier
     */
    public final String getId() {

        return getValue(AcraReportHeader.REPORT_ID);
    }

    /**
     * Gets the MD5 hash of the stacktrace field.
     * 
     * @return the MD5 hash of the stacktrace field
     */
    public final String getStacktraceMD5() {

        return stacktraceMD5;
    }

    /**
     * Gets the {@link AcraReportHeader#USER_CRASH_DATE} field value.
     * 
     * @return the user crash date
     */
    public final Date getUserCrashDate() {

        return userCrashDate;
    }
}
