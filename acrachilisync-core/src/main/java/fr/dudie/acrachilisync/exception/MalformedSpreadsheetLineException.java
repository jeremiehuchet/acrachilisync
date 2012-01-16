package fr.dudie.acrachilisync.exception;

import fr.dudie.acrachilisync.model.AcraReport;
import fr.dudie.acrachilisync.model.AcraReportHeader;

/**
 * Thrown when the given elements collections is missing some tags and/or values to instantiates an
 * {@link AcraReport}.
 * 
 * @author Jérémie Huchet
 */
public class MalformedSpreadsheetLineException extends Exception {

    /** The serial version UID. */
    private static final long serialVersionUID = -5659618864509626793L;

    /** The missing header or the header having a wrong value. */
    private final AcraReportHeader header;

    /** The value, null when the value is missing. */
    private final String value;

    /**
     * Constructor.
     * 
     * @param pHeader
     *            the missing header or the header having a wrong value
     * @param pValue
     *            the value, null when the value is missing
     */
    public MalformedSpreadsheetLineException(final AcraReportHeader pHeader, final String pValue) {

        this.header = pHeader;
        this.value = pValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {

        return String.format("wrong value for '%s'='%s'", header, value);
    }
}
