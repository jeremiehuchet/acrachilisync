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
