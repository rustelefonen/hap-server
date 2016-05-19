package no.rustelefonen.hapserver.entities;

import javax.persistence.*;

import static no.rustelefonen.hapserver.entities.ResearchUser.SEQUENCE_NAME;

/**
 * Created by lon on 05/04/16.
 */
@SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, initialValue = 50)
@Entity
@NamedQueries({
        @NamedQuery(name = ResearchUser.GET_ALL, query = "select u from ResearchUser u ORDER BY u.id DESC")
})
public class ResearchUser {
    public enum Gender {MALE, FEMALE, OTHER, NOT_SPECIFIED}
    public static final String GET_ALL = "ResearchUser.getAll";
    public static final String SEQUENCE_NAME = "ResearchUser_Sequence";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private long id;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}