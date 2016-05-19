package no.rustelefonen.hapserver.api;

import no.rustelefonen.hapserver.ejb.ResearchUserEjb;
import no.rustelefonen.hapserver.entities.ResearchUser;
import no.rustelefonen.hapserver.entities.ResearchUser.Gender;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static com.google.common.base.Enums.getIfPresent;
import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.primitives.Ints.tryParse;

/**
 * Created by lon on 12/04/16.
 */
@Path("/research")
public class ResearchService {

    @Inject
    private ResearchUserEjb researchUserEjb;

    @POST
    public Response sendResearch(@DefaultValue("-1") @FormParam("age") String age,
                                 @DefaultValue("NOT_SPECIFIED") @FormParam("gender") String gender,
                                 @DefaultValue("NOT_SPECIFIED") @FormParam("county") String county) {
        ResearchUser researchUser = new ResearchUser();

        researchUser.setAge(firstNonNull(tryParse(age), -1));
        researchUser.setGender(getIfPresent(Gender.class, gender).or(Gender.NOT_SPECIFIED));
        researchUser.setState(county);

        System.out.println(researchUser.getState());
        researchUserEjb.persist(researchUser);

        if (researchUser.getId() > 0) return Response.created(null).build();
        else return Response.serverError().build();
    }
}
