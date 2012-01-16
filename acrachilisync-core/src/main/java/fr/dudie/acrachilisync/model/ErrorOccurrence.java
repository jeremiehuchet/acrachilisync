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

import java.util.Date;

/**
 * Metadata for an error occurrence
 * 
 * @author Jérémie Huchet
 */
public class ErrorOccurrence {

    /** The report identifier. */
    private String reportId;

    /** The date when the crash occured. */
    private Date crashDate;

    /** The android version running the application. */
    private String androidVersion;

    /** The application version code. */
    private String versionCode;

    /** The application version name. */
    private String versionName;

    /** Informations about the device running the application. */
    private String device;

    /**
     * Gets the reportId.
     * 
     * @return the reportId
     */
    public final String getReportId() {

        return reportId;
    }

    /**
     * Sets the reportId.
     * 
     * @param reportId
     *            the reportId to set
     */
    public final void setReportId(final String reportId) {

        this.reportId = reportId;
    }

    /**
     * Gets the crashDate.
     * 
     * @return the crashDate
     */
    public final Date getCrashDate() {

        return crashDate;
    }

    /**
     * Sets the crashDate.
     * 
     * @param crashDate
     *            the crashDate to set
     */
    public final void setCrashDate(final Date crashDate) {

        this.crashDate = crashDate;
    }

    /**
     * Gets the androidVersion.
     * 
     * @return the androidVersion
     */
    public final String getAndroidVersion() {

        return androidVersion;
    }

    /**
     * Sets the androidVersion.
     * 
     * @param androidVersion
     *            the androidVersion to set
     */
    public final void setAndroidVersion(final String androidVersion) {

        this.androidVersion = androidVersion;
    }

    /**
     * Gets the versionCode.
     * 
     * @return the versionCode
     */
    public final String getVersionCode() {

        return versionCode;
    }

    /**
     * Sets the versionCode.
     * 
     * @param versionCode
     *            the versionCode to set
     */
    public final void setVersionCode(final String versionCode) {

        this.versionCode = versionCode;
    }

    /**
     * Gets the versionName.
     * 
     * @return the versionName
     */
    public final String getVersionName() {

        return versionName;
    }

    /**
     * Sets the versionName.
     * 
     * @param versionName
     *            the versionName to set
     */
    public final void setVersionName(final String versionName) {

        this.versionName = versionName;
    }

    /**
     * Gets the device.
     * 
     * @return the device
     */
    public final String getDevice() {

        return device;
    }

    /**
     * Sets the device.
     * 
     * @param device
     *            the device to set
     */
    public final void setDevice(final String device) {

        this.device = device;
    }

}
