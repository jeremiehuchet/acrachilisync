package fr.dudie.acrachilisync;

import java.io.IOException;
import java.text.ParseException;

import org.redmine.ta.AuthenticationException;
import org.redmine.ta.NotFoundException;
import org.redmine.ta.RedmineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.util.ServiceException;

/**
 * @author Jérémie Huchet
 */
public final class AcraChiliSync {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AcraChiliSync.class);

    /**
     * @param args
     * @throws RedmineException
     * @throws NotFoundException
     * @throws AuthenticationException
     * @throws ServiceException
     * @throws IOException
     * @throws ParseException
     */
    public static void main(final String[] args) throws IOException, ServiceException,
            AuthenticationException, NotFoundException, RedmineException, ParseException {

        final AcraToChiliprojectSyncer syncer = new AcraToChiliprojectSyncer();
        syncer.startSynchronization();
    }
}
