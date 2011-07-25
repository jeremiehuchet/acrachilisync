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
