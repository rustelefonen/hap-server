package no.rustelefonen.hapserver.ejb;

import no.rustelefonen.hapserver.entities.Category;
import no.rustelefonen.hapserver.entities.Info;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Fredrik on 05.04.2016.
 */
@Stateless
public class InfoEjb {

    @PersistenceContext(unitName = "PG6100")
    private EntityManager entityManager;

    public InfoEjb(){}

    InfoEjb(EntityManager entityManager){this.entityManager = entityManager;}

    public Category persist(Category category){
        category.setVersionNumber(category.getVersionNumber() + 1);
        entityManager.persist(category);
        return category;
    }

    public Info persist(Info info, Category category){
        category.setVersionNumber(category.getVersionNumber() + 1);
        info.setCategory(category);
        entityManager.merge(category);
        entityManager.persist(info);
        return info;
    }

    public void update(Category category) {
        category.setVersionNumber(category.getVersionNumber() + 1);
        entityManager.merge(category);
    }

    public void update(Info info, int newCategoryId) {
        Category currentCategory = info.getCategory();
        currentCategory.setVersionNumber(currentCategory.getVersionNumber() + 1);

        if(newCategoryId != currentCategory.getId()){
            currentCategory.getInfoList().remove(info);
            Category newCategory = getCategoryById(newCategoryId);
            newCategory.setVersionNumber(newCategory.getVersionNumber() + 1);
            info.setCategory(newCategory);
            entityManager.merge(newCategory);
        }

        entityManager.merge(currentCategory);
        entityManager.merge(info);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Category getCategoryById(int id){
        return entityManager.find(Category.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Info getInfoById(int id){
        return entityManager.find(Info.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Category> getAllCateogires(){
        return entityManager.createNamedQuery(Category.GET_ALL, Category.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Info> getAllInfos() {
        return entityManager.createNamedQuery(Info.GET_ALL, Info.class).getResultList();
    }

    public void deleteCategoryById(int id) {
        Category categoryToDelete = getCategoryById(id);
        entityManager.remove(categoryToDelete);
    }

    public void deleteInfoById(int id) {
        Info info = getInfoById(id);
        Category category = info.getCategory();
        category.setVersionNumber(category.getVersionNumber() + 1);

        category.getInfoList().remove(info);
        entityManager.merge(category);
        entityManager.remove(info);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean categoryWithOrderNumberExists(int orderNumber){
        return entityManager.createNamedQuery(Category.GET_BY_ORDER_NUMBER, Category.class)
                .setParameter(Category.ORDER_PARAM, orderNumber)
                .getResultList().size() > 0;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean categoryTitleExists(String title) {
        return entityManager.createNamedQuery(Category.GET_BY_TITLE, Category.class)
                .setParameter(Category.TITLE_PARAM, title)
                .getResultList().size() > 0;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean infoTitleExists(String title) {
        return entityManager.createNamedQuery(Info.GET_BY_TITLE, Info.class)
                    .setParameter(Info.TITLE_PARAM, title)
                    .getResultList().size() > 0;
    }
}