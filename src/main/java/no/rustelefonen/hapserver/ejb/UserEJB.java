package no.rustelefonen.hapserver.ejb;



import no.rustelefonen.hapserver.entities.User;
import no.rustelefonen.hapserver.utilities.Security;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * Created by Simen Fonnes on 27.02.2016.
 */
@Stateless
public class UserEJB implements Serializable{

    @PersistenceContext(unitName = "PG6100")
    private EntityManager entityManager;

    UserEJB(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public UserEJB() {}

    public User getById(long id) {
        return entityManager.find(User.class, id);
    }

    public boolean login(User user, String password) {
        if (user == null) return false;
        user = getByUsername(user.getUsername());
        if (user == null) return false;

        String hash = Security.generateHash(password, user.getSalt());
        return hash.equals(user.getHash());
    }

    private User getByUsername(String username) {
        try {
            return entityManager.createNamedQuery(User.GET_BY_USERNAME, User.class)
                    .setParameter(User.USERNAME_PARAM, username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
