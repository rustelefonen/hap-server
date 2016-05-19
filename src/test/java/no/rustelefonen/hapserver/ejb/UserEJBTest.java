package no.rustelefonen.hapserver.ejb;

import no.rustelefonen.hapserver.entities.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

/**
 * Created by Simen Fonnes on 08.05.2016.
 */
public class UserEJBTest {

    private EntityManager entityManager;
    private UserEJB userEJB;

    @Before
    public void setUp() throws Exception {
        entityManager = Persistence.createEntityManagerFactory("PG6100").createEntityManager();
        userEJB = new UserEJB(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        entityManager.close();
    }

    @Test
    public void getById() {
        assertNotNull(userEJB.getById(1));
    }

    @Test
    public void getByWrongId() {
        assertNull(userEJB.getById(2));
    }

    @Test
    public void loginWithNullUser() throws Exception {
        assertFalse(userEJB.login(null, "admin"));
    }

    @Test
    public void loginWithInvalidUsername() throws Exception {
        User user = new User();
        user.setUsername("test");
        assertFalse(userEJB.login(user, "admin"));
    }

    @Test
    public void login() throws Exception {
        User user = new User();
        user.setUsername("admin");
        assertTrue(userEJB.login(user, "admin"));
    }
}
