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

import java.util.Comparator;

import org.redmine.ta.beans.Issue;

/**
 * @author Jérémie Huchet
 */
public class CreationDateIssueComparator implements Comparator<Issue> {

    /**
     * Compare creation date of given issues.
     * 
     * @see java.util.Date#compareTo(java.util.Date)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final Issue i1, final Issue i2) {

        if (null == i1 && null == i2) {
            return 0;
        } else if (null == i1) {
            return 1;
        } else if (null == i2) {
            return -1;
        } else {
            return i1.getCreatedOn().compareTo(i2.getCreatedOn());
        }
    }
}
