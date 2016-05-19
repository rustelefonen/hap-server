package no.rustelefonen.hapserver.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Simen Fonnes on 27.02.2016.
 */
@NamedQueries({
        @NamedQuery(name = User.GET_BY_USERNAME, query = "SELECT u FROM User u WHERE u.username = :" + User.USERNAME_PARAM)
})
@SequenceGenerator(name = User.SEQUENCE_NAME, sequenceName = User.SEQUENCE_NAME, initialValue = 50)
@Entity
public class User {
    public static final String USERNAME_PARAM = "User_username";
    public static final String SEQUENCE_NAME = "User_Sequence";
    public static final String GET_BY_USERNAME = "User.getByUsername";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private long id;

    @NotNull
    @Size(min = 3, max = 60)
    private String username;

    @NotNull
    private String hash;

    @NotNull
    @Size(max = 26)
    private String salt;

    public User() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
