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

import org.apache.commons.collections.Predicate;
import org.redmine.ta.beans.CustomField;

/**
 * @author Jérémie Huchet
 */
public final class CustomFieldIdPredicate implements Predicate {

    /** The expected custom field id. */
    private final int customFieldId;

    /**
     * Constructor.
     * 
     * @param pCustomFieldId
     *            the expected custom field id
     */
    public CustomFieldIdPredicate(final int pCustomFieldId) {

        this.customFieldId = pCustomFieldId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    @Override
    public boolean evaluate(final Object object) {

        boolean result = false;
        if (object instanceof CustomField) {
            result = customFieldId == ((CustomField) object).getId();
        }
        return result;
    }
}
