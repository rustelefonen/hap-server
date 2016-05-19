package no.rustelefonen.hapserver.controllers;

import no.rustelefonen.hapserver.ejb.InfoEjb;
import no.rustelefonen.hapserver.entities.Category;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static no.rustelefonen.hapserver.controllers.CategoryController.StatusCode.ADDED;
import static no.rustelefonen.hapserver.controllers.CategoryController.StatusCode.DELETED;
import static no.rustelefonen.hapserver.controllers.CategoryController.StatusCode.EDITED;

/**
 * Created by simenfonnes on 14.04.2016.
 * Refacced by Fredrik Loberg
 */
@Named
@ViewScoped
public class CategoryController implements Serializable {
    public enum StatusCode {ADDED, EDITED, DELETED}

    private int categoryId;
    private Category category;

    private List<Category> categoryList;
    private StatusCode statusCode;

    @Inject
    private InfoEjb infoEjb;

    @PostConstruct
    public void construct() {
        category = new Category();
    }

    public void setNextOrderNumberOnCategory(){
        category.setOrderNumber(infoEjb.getAllCateogires().stream()
                .mapToInt(Category::getOrderNumber)
                .max()
                .orElse(0)
                + 1);
    }

    public void loadAllCategories() {
        categoryList = infoEjb.getAllCateogires();
    }

    public void initCategoryById() {
        category = infoEjb.getCategoryById(categoryId);
    }

    public String addInfoCategory() {
        String errorMsg = infoEjb.categoryTitleExists(category.getTitle()) ? "Kategorinavnet er allerede i bruk"
                : infoEjb.categoryWithOrderNumberExists(category.getOrderNumber()) ? "Sorteringsnummeret er allerede i bruk"
                : null;

        if(errorMsg != null){
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMsg, ""));
            return null;
        }
        else {
            infoEjb.persist(category);
            return "/category/show_categories.jsf?faces-redirect=true&statusCode=" + ADDED;
        }
    }

    public String updateCategory() {
        Category categoryInDb = infoEjb.getCategoryById(category.getId());

        String errorMsg = anotherCategoryWithSameTitleExists(category, categoryInDb) ? "Kategorinavnet er allerede i bruk"
                : anotherCategoryWithSameOrderNumberExists(category, categoryInDb) ? "Sorteringsnummeret er allerede i bruk"
                : null;

        if(errorMsg != null){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMsg, ""));
            return null;
        }
        else{
            infoEjb.update(category);
            return "/category/show_categories.jsf?faces-redirect=true&statusCode=" + EDITED;
        }
    }

    public void deleteInfoCategory(int id) {
        infoEjb.deleteCategoryById(id);
        categoryList.removeIf(c -> c.getId() == id);

        statusCode = DELETED;
        addFacesMessage();
    }

    public void addFacesMessage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String statusMsg = statusCode == ADDED ? "Kategorien ble lagt til."
                : statusCode == EDITED ? "Kategorien ble redigert."
                : statusCode == DELETED ? "Kategorien ble slettet."
                : null;

        if (statusMsg != null) fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", statusMsg));
    }

    private boolean anotherCategoryWithSameTitleExists(Category category, Category originalFromDb){
        return !originalFromDb.getTitle().equals(category.getTitle()) &&
                infoEjb.categoryTitleExists(category.getTitle());
    }

    private boolean anotherCategoryWithSameOrderNumberExists(Category category, Category originalFromDb){
        return originalFromDb.getOrderNumber() != category.getOrderNumber() &&
                infoEjb.categoryWithOrderNumberExists(category.getOrderNumber());
    }

//    GETTERS AND SETTERS
    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
