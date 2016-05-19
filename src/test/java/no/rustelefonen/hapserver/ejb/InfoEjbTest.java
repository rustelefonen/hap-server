package no.rustelefonen.hapserver.ejb;

import no.rustelefonen.hapserver.entities.Category;
import no.rustelefonen.hapserver.entities.Info;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

/**
 * Created by Simen Fonnes on 09.05.2016.
 */
public class InfoEjbTest {

    private EntityManager entityManager;
    private InfoEjb infoEjb;

    @Before
    public void setUp() throws Exception {
        entityManager = Persistence.createEntityManagerFactory("PG6100").createEntityManager();
        infoEjb = new InfoEjb(entityManager);
    }

    @After
    public void tearDown() throws Exception {
        entityManager.close();
    }

    @Test
    public void persistCategory() throws Exception {
        Category category = new Category();
        category.setTitle("Test");
        category.setOrderNumber(100);
        category.setVersionNumber(100);

        Category categoryFromDb = infoEjb.persist(category);
        assertNotNull(categoryFromDb);
        assertEquals(category.getVersionNumber(), 101);
    }

    @Test
    public void persistInfo() throws Exception {
        Category category = infoEjb.getCategoryById(1);
        int versionNumber = category.getVersionNumber();

        Info info = new Info();
        info.setTitle("Test");

        Info infoFromDb = infoEjb.persist(info, category);
        assertNotNull(infoFromDb);

        Category categoryFromDb = infoEjb.getCategoryById(1);
        assertEquals(++versionNumber, categoryFromDb.getVersionNumber());
    }

    @Test
    public void updateCategory() throws Exception {
        Category category = infoEjb.getCategoryById(1);
        int versionNumber = category.getVersionNumber();
        String newName = "Test";
        category.setTitle(newName);

        infoEjb.update(category);

        Category categoryFromDb = infoEjb.getCategoryById(1);
        assertEquals(++versionNumber, categoryFromDb.getVersionNumber());
        assertEquals(newName, categoryFromDb.getTitle());
    }

    @Test
    public void updateInfo() throws Exception {
        Info info = infoEjb.getInfoById(1);
        int versionNumber = info.getCategory().getVersionNumber();
        int newCategoryId = 2;
        infoEjb.update(info, newCategoryId);

        Info infoFromDb = infoEjb.getInfoById(1);
        assertEquals(++versionNumber, infoFromDb.getCategory().getVersionNumber());
        assertEquals(newCategoryId, infoFromDb.getCategory().getId());
    }

    @Test
    public void getCategoryById() throws Exception {
        assertNotNull(infoEjb.getCategoryById(1));
    }

    @Test
    public void getInfoById() throws Exception {
        assertNotNull(infoEjb.getInfoById(1));
    }

    @Test
    public void getAllCategories() throws Exception {
        assertTrue(infoEjb.getAllCateogires().size() > 0);
    }

    @Test
    public void getAllInfos() throws Exception {
        assertTrue(infoEjb.getAllInfos().size() > 0);
    }

    @Test
    public void deleteCategoryById() throws Exception {
        infoEjb.deleteCategoryById(1);
        assertNull(infoEjb.getInfoById(1));
    }

    @Test
    public void deleteInfoById() throws Exception {
        Info info = infoEjb.getInfoById(1);
        infoEjb.deleteInfoById(info.getId());
        assertNull(infoEjb.getInfoById(1));

        Category category = infoEjb.getCategoryById(info.getCategory().getId());
        assertFalse(category.getInfoList().contains(info));
    }

    @Test
    public void categoryWithOrderNumberExists() throws Exception {
        assertTrue(infoEjb.categoryWithOrderNumberExists(1));
    }

    @Test
    public void categoryTitleExists() throws Exception {
        String title = "Råd og Tips";
        assertTrue(infoEjb.categoryTitleExists(title));
    }

    @Test
    public void infoTitleExists() throws Exception {
        String title = "Motivasjon";
        assertTrue(infoEjb.infoTitleExists(title));
    }
}