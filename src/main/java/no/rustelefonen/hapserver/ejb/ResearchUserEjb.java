package no.rustelefonen.hapserver.ejb;

import no.rustelefonen.hapserver.entities.ResearchUser;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lon on 12/04/16.
 */

@Stateless
public class ResearchUserEjb {

    @PersistenceContext(unitName = "PG6100")
    private EntityManager entityManager;
    private static final String COMMA = ",";
    private static final String NEW_LINE = "\n";
    private static final String FILE_HEADER = "age,gender,state,userType";

    public ResearchUserEjb(){}

    public ResearchUserEjb(EntityManager entityManager){this.entityManager = entityManager;}

    public ResearchUser persist(ResearchUser researchUser){
        entityManager.persist(researchUser);
        return researchUser;
    }

    public ResearchUser getResearchUserById(long id) {
        return entityManager.find(ResearchUser.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    private List<ResearchUser> getAllResearchUsers(){
        return entityManager.createNamedQuery(ResearchUser.GET_ALL, ResearchUser.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<ResearchUser> getMostRecentResearchUsers() {
        return entityManager.createNamedQuery(ResearchUser.GET_ALL, ResearchUser.class)
                .setMaxResults(30)
                .getResultList();
    }

    public StreamedContent getCsvFile() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("sep=,");
        stringBuilder.append(NEW_LINE);
        stringBuilder.append(FILE_HEADER);
        stringBuilder.append(NEW_LINE);
        for (ResearchUser researchUser : getAllResearchUsers()) {
            stringBuilder.append(String.valueOf(researchUser.getAge()));
            stringBuilder.append(COMMA);
            ResearchUser.Gender g = researchUser.getGender();
            stringBuilder.append(g == ResearchUser.Gender.MALE ? "Mann"
                    : g == ResearchUser.Gender.FEMALE ? "Kvinne"
                    : g == ResearchUser.Gender.OTHER ? "Annet"
                    : "Ikke spesifisert");
            stringBuilder.append(COMMA);
            stringBuilder.append(researchUser.getState());
            stringBuilder.append(COMMA);
            stringBuilder.append(researchUser.getUserType());
            stringBuilder.append(NEW_LINE);
        }

        InputStream stream = new ByteArrayInputStream(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
        return new DefaultStreamedContent(stream, "text/csv",
                new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss").format(new Date()) + "--appforskning.csv");
    }
}