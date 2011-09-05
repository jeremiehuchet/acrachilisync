package fr.dudie.acrachilisync.model;

import java.io.IOException;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.util.ServiceException;

/**
 * @author Jérémie Huchet
 */
public final class EditableAcraReport extends AcraReport {

    /** The Google spreadsheet list entry. */
    private final ListEntry listEntry;

    /**
     * Constructor.
     * 
     * @param pEntry
     *            the Google spreadsheet list entry
     */
    public EditableAcraReport(final ListEntry pEntry) {

        super(pEntry.getCustomElements());
        this.listEntry = pEntry;
    }

    /**
     * Updates the stacktrace MD5 hash on the remote Google spreadsheet.
     * 
     * @throws ServiceException
     * @throws IOException
     */
    public void commitStacktraceMD5() throws IOException, ServiceException {

        listEntry.getCustomElements().setValueLocal(AcraReportHeader.STACK_TRACE_MD5.tagName(),
                getStacktraceMD5());
        listEntry.update();
    }
}
