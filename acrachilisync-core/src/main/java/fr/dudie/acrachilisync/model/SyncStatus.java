package fr.dudie.acrachilisync.model;

/**
 * @author Jérémie Huchet
 */
public enum SyncStatus {

    /** Synchronization not yet started. */
    NOT_STARTED,

    /** Status synchronization in progress. */
    IN_PROGRESS,

    /** Status synchronized successfully. */
    SUCCESS,

    /** Status not synchronized because of errors. */
    FAILURE
}
