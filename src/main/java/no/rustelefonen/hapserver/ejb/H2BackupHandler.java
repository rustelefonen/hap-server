package no.rustelefonen.hapserver.ejb;

import org.h2.tools.Script;
import org.hibernate.Session;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by simenfonnes on 20.04.2016.
 */

@Stateless
public class H2BackupHandler {

    @PersistenceContext(unitName = "PG6100")
    private EntityManager entityManager;

    /**
     * Hardcoded Hibernate with H2 backup
     */
    @Schedule(hour = "3")
    public void backupDb() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date now = new Date();
        String strDate = sdf.format(now);

        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> Script.process(connection, "dbbackups/" + strDate + ".sql", "SIMPLE", ""));
        System.out.println(strDate + ".sql");
    }
}
