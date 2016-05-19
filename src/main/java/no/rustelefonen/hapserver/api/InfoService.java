package no.rustelefonen.hapserver.api;

import no.rustelefonen.hapserver.ejb.InfoEjb;
import no.rustelefonen.hapserver.entities.Category;
import no.rustelefonen.hapserver.entities.Category.CategoryList;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lon on 05/04/16.
 */
@Path("/info")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class InfoService {

    @Inject
    private InfoEjb infoEjb;


    //Legge til hash med not modified
    @GET
    @Path("/categories/all")
    public CategoryList getAllCategories(){
        return new CategoryList(infoEjb.getAllCateogires());
    }

    @GET
    @Path("/categories/{id}")
    public Category getById(@PathParam("id") int id){
        return infoEjb.getCategoryById(id);
    }

    @GET
    @Path("/version")
    public Category.SummaryList checkVersionNumber() {
        List<Category> categories = infoEjb.getAllCateogires();
        return categories.stream()
                .map(category -> new Category.Summary(category.getId(), category.getVersionNumber()))
                .collect(Collectors.toCollection(Category.SummaryList::new));
    }
}
