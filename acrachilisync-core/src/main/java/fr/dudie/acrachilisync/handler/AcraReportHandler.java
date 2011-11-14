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

import org.redmine.ta.beans.Issue;

import fr.dudie.acrachilisync.exception.SynchronizationException;
import fr.dudie.acrachilisync.model.AcraReport;

/**
 * Provide a way to add behavior when synchronizing Acra reports.
 * 
 * @author Jérémie Huchet
 */
public interface AcraReportHandler {

    /**
     * Triggered for each new report found.
     * 
     * @param pReport
     *            the new report
     * @throws SynchronizationException
     *             process error
     */
    void onNewReport(AcraReport pReport) throws SynchronizationException;

    /**
     * Triggered for each report for issues we already know and already synchronized.
     * 
     * @param report
     *            the acra report
     * @param issue
     *            the Chiliproject existing issue
     * @throws SynchronizationException
     *             process error
     */
    void onKnownIssueAlreadySynchronized(AcraReport pReport, Issue pIssue)
            throws SynchronizationException;

    /**
     * Triggered for each report for issues we already know and not yed synchronized.
     * 
     * @param report
     *            the acra report
     * @param issue
     *            the Chiliproject existing issue
     * @throws SynchronizationException
     *             process error
     */
    void onKnownIssueNotSynchronized(AcraReport pReport, Issue pIssue)
            throws SynchronizationException;

    /**
     * Triggered when all new reports have been synchronized with Chiliproject bugtracker.
     * 
     * @throws SynchronizationException
     *             process error
     */
    void onFinishReceivingNewReports() throws SynchronizationException;
}
