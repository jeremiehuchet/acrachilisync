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

package fr.dudie.acrachilisync.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dudie.acrachilisync.model.ErrorOccurrence;

/**
 * Helper class to build a formatted issue description to be able to retrieve informations later.
 * 
 * @author Jérémie Huchet
 * @see IssueDescriptionReader
 */
public final class IssueDescriptionBuilder {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueDescriptionBuilder.class);

    /** The error stacktrace. */
    private final String stacktrace;

    /** The error stacktrace MD5 hash. */
    private final String stacktraceMD5;

    /** The occurrence time for each ACRA bug report. */
    private final List<ErrorOccurrence> occurrences;

    /**
     * Default constructor.
     * 
     * @param pStacktrace
     *            the stacktrace
     */
    public IssueDescriptionBuilder(final String pStacktrace) {

        stacktrace = pStacktrace;
        stacktraceMD5 = MD5Utils.toMD5hash(pStacktrace);
        occurrences = new ArrayList<ErrorOccurrence>();
    }

    /**
     * Builds the description.
     * 
     * @return the issue description
     * @see IssueDescriptionUtils
     */
    public String build() {

        final StringBuilder description = new StringBuilder();

        // append occurrences
        description.append(IssueDescriptionUtils.getOccurrencesTableHeader()).append('\n');
        for (final ErrorOccurrence error : occurrences) {
            description.append(IssueDescriptionUtils.getOccurrencesTableLine(error));
            description.append('\n');
        }

        // append stacktrace
        description.append("\n\n");
        description.append("*Stacktrace*").append('\n');
        description.append("<pre class=\"javastacktrace\">");
        description.append(stacktrace.trim()).append("</pre>");

        // append version
        description.append('\n').append(IssueDescriptionUtils.DESCRIPTION_VERSION_TAG);

        return description.toString();
    }

    /**
     * Sets the list of occurrences for this bug.
     * 
     * @param pOccurrences
     *            a list with the bug occurrence informations
     * @see #addOccurrence(String, Date)
     */
    public void setOccurrences(final List<ErrorOccurrence> pOccurrences) {

        occurrences.clear();
        for (final ErrorOccurrence error : pOccurrences) {
            addOccurrence(error);
        }
    }

    /**
     * Adds an occurrence for this bug.
     * 
     * @param pError
     *            the ACRA error informations
     */
    public void addOccurrence(final ErrorOccurrence pError) {

        occurrences.add(pError);
    }
}
