package fr.dudie.acrachilisync.exception;

import java.io.IOException;

/**
 * Thrown when an exception occurs while parsing the content of an issue.
 * 
 * @author Jérémie Huchet
 */
public final class IssueParseException extends IOException {

    /** The serial version UID. */
    private static final long serialVersionUID = -8994735126753225851L;

    /**
     * Constructor.
     * 
     * @param pMessage
     *            the error message
     */
    public IssueParseException(final String pMessage) {

        this(pMessage, null);
    }

    /**
     * Constructor.
     * 
     * @param pMessage
     *            the error message
     * @param pCause
     *            the error cause
     */
    public IssueParseException(final String pMessage, final Throwable pCause) {

        super(pMessage, pCause);
    }
}
