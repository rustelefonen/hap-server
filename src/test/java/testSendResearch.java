import no.rustelefonen.hapserver.entities.ResearchUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by martinnikolaisorlie on 13/04/16.
 */
public class testSendResearch {

    private EntityManagerFactory factory;
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception{
        factory = Persistence.createEntityManagerFactory("PG6100");
        entityManager = factory.createEntityManager();
    }

    @After
    public void tearDown() throws Exception{
        entityManager.close();
        factory.close();
    }

    @Test
    public void testStateString() throws Exception{
        ResearchUser user = new ResearchUser();
        user.setState("Østfold");

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        List<ResearchUser> list =  entityManager.createNamedQuery(ResearchUser.GET_ALL, ResearchUser.class).getResultList();
        System.out.println(list.get(0).getState());

        assertEquals("Østfold", user.getState());
    }

    @Test
    public void testUserCreation() throws Exception{
        ResearchUser user = new ResearchUser();
        user.setAge(16);
        user.setGender(ResearchUser.Gender.MALE);
        user.setState("Akershus");

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        assertTrue(user.getId() > 0);
    }
}
