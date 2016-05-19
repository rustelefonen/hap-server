package no.rustelefonen.hapserver.controllers;

import no.rustelefonen.hapserver.ejb.ResearchUserEjb;
import no.rustelefonen.hapserver.entities.ResearchUser;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by simenfonnes on 21.04.2016.
 */
@Named
@RequestScoped
public class ResearchDataController {

    private List<ResearchUser> researchUserList;

    @Inject
    private ResearchUserEjb researchUserEjb;

    @PostConstruct
    public void construct() {
        researchUserList = researchUserEjb.getMostRecentResearchUsers();
    }

    public List<ResearchUser> getResearchUserList() {
        return researchUserList;
    }

    public void setResearchUserList(List<ResearchUser> researchUserList) {
        this.researchUserList = researchUserList;
    }

    public StreamedContent getFile() {
        return researchUserEjb.getCsvFile();
    }
}
