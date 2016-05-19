package no.rustelefonen.hapserver.controllers;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Simen Fonnes on 09.05.2016.
 */
public class CategoryControllerIT extends HapTest {

    private static final String TEST_CATEGORY_NAME = "TestKategori";

    @Test
    public void addNewCategory() {
        addTestCategory();
        assertNotNull(webDriver.findElement(By.xpath("//label[text()='" + TEST_CATEGORY_NAME + "']")));
        waitForPageToLoad();
        //Cleanup
        deleteTestCategory();
    }

    @Test
    public void editNewCategory() {
        addTestCategory();
        waitForPageToLoad();
        webDriver.findElement(By.xpath("//tr[@data-ri='9']//span[text()='Detaljer']")).click();
        waitForPageToLoad();
        WebElement title = webDriver.findElement(By.id("j_idt17:j_idt22"));
        title.clear();
        title.sendKeys("TestKategori2");
        webDriver.findElement(By.xpath("//span[text()='Lagre']")).click();
        waitForPageToLoad();
        assertNotNull(webDriver.findElement(By.xpath("//label[text()='TestKategori2']")));
        waitForPageToLoad();
        //Cleanup
        deleteTestCategory();
    }

    @Test
    public void deleteCategory() {
        addTestCategory();
        waitForPageToLoad();
        deleteTestCategory();
        List<WebElement> deletedElement = webDriver.findElements(By.xpath("//label[text()='" + TEST_CATEGORY_NAME + "']"));
        assertTrue(deletedElement.isEmpty());
    }

    private void addTestCategory() {
        webDriver.findElement(By.xpath("//*[contains(@class,'ui-button-text ui-c')]")).click();
        waitForPageToLoad();
        webDriver.findElement(By.id("j_idt16:j_idt21")).sendKeys(TEST_CATEGORY_NAME);
        webDriver.findElement(By.xpath("//*[contains(@class,'ui-button-text ui-c')]")).click();
        waitForPageToLoad();
    }

    private void deleteTestCategory() {
        webDriver.findElement(By.xpath("//tr[@data-ri='9']//span[text()='Slett']")).click();
        waitForPageToLoad();
        webDriver.findElement(By.xpath("//*[contains(@class, 'ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left ui-confirmdialog-yes')]//span[text()='Ja']")).click();
        waitForPageToLoad();
    }
}
