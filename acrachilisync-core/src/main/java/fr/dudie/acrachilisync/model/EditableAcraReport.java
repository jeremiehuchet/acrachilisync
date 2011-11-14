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
