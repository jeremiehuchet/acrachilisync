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
