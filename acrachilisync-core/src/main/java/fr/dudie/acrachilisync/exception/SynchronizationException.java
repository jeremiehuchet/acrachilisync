package fr.dudie.acrachilisync.exception;

/**
 * Thrown when a synchronization step fails.
 * 
 * @author Jérémie Huchet
 */
public final class SynchronizationException extends Exception {

    /** The servial version UID. */
    private static final long serialVersionUID = 7694929045131452851L;

    /**
     * Constructor.
     * 
     * @param pDescription
     *            the error description
     * @param pCause
     *            the error cause
     */
    public SynchronizationException(final String pDescription, final Throwable pCause) {

        super(pDescription, pCause);
    }
}
