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
