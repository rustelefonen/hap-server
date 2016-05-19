package no.rustelefonen.hapserver.ejb;

import no.rustelefonen.hapserver.entities.ResearchUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Simen Fonnes on 09.05.2016.
 */
public class ResearchUserEjbTest {

    private EntityManager entityManager;
    private ResearchUserEjb researchUserEjb;

    @Before
    public void setUp() throws Exception {
        entityManager = Persistence.createEntityManagerFactory("PG6100").createEntityManager();
        researchUserEjb = new ResearchUserEjb(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        entityManager.close();
    }

    @Test
    public void persist() throws Exception {
        ResearchUser researchUser = new ResearchUser();
        researchUser.setAge(18);
        researchUser.setGender(ResearchUser.Gender.FEMALE);
        researchUser.setState("Finnmark");
        assertNotNull(researchUserEjb.persist(researchUser));
    }

    @Test
    public void getResearchUserById() throws Exception {
        long id = 1;
        assertNotNull(researchUserEjb.getResearchUserById(id));
    }

    @Test
    public void getAllResearchUsers() throws Exception {
        assertTrue(researchUserEjb.getMostRecentResearchUsers().size() > 0);
    }
}
