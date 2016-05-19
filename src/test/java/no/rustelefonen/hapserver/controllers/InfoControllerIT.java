package no.rustelefonen.hapserver.controllers;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by simenfonnes on 10.05.2016.
 */
public class InfoControllerIT extends HapTest {

    private static final String BEFORE = "Legg til bilde\nLegg til bilde\nKategori\nRåd og Tips\nLagre";
    private static final String AFTER = "Kategorier\nInformasjonssider\nForskningsdata\nLogg ut\n" +
            "Innlogget som admin\nRediger informasjonsside\nTittel\nInnhold";
    private static final String I_FRAME = "//iframe";

    @Test
    public void addNewInfo() throws FileNotFoundException, InterruptedException {
        navigateToInfoPage();
        addTestInfo();
        assertNotNull(webDriver.findElement(By.xpath("//label[text()='TestInfoSide']")));
        navigateToInfoPageUsingDetails();
        waitForPageToLoad();
        assertNotNull(webDriver.findElement(By.xpath("//*[contains(@class, 'ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all')]")));
        webDriver.findElements(By.xpath("//*[contains(@class, 'ui-editor-button')]")).get(31).click();
        String newKek = webDriver.findElement(By.cssSelector("body")).getText().replace(BEFORE, "").replace(AFTER, "").trim();
        assertEquals(newKek, new Scanner(new File("./src/test/resources/infoContent.txt")).useDelimiter("\\Z").next());
        webDriver.findElement(By.xpath("//span[text()='Informasjonssider']")).click();
        waitForPageToLoad();
        deleteTestInfo();
    }

    @Test
    public void deleteNewInfo() throws InterruptedException {
        navigateToInfoPage();
        addTestInfo();
        navigateToInfoPageUsingDetails();
        webDriver.findElements(By.xpath("//*[contains(@class, 'ui-editor-button')]")).get(31).click();
    }

    @Test
    public void editNewInfo() throws InterruptedException {
        navigateToInfoPage();
        addTestInfo();
        waitForPageToLoad();
        navigateToInfoPageUsingDetails();
        WebElement webElement = webDriver.findElement(By.xpath("//*[contains(@class, 'ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all')]"));
        webElement.clear();
        webElement.sendKeys("TestInfoSide2");
        webDriver.findElement(By.xpath("//span[text()='Lagre']")).click();
        waitForPageToLoad();
        assertNotNull(webDriver.findElement(By.xpath("//tr[@data-ri='5']//label[text()='TestInfoSide2']")));
        deleteTestInfo();
    }

    private void navigateToInfoPage() {
        webDriver.findElement(By.xpath("//span[text()='Informasjonssider']")).click();
        waitForPageToLoad();
    }

    private void navigateToInfoPageUsingDetails() {
        waitForPageToLoad();
        webDriver.findElement(By.xpath("//tr[@data-ri='5']//span[text()='Detaljer']")).click();
        waitForPageToLoad();
    }

    private void addTestInfo() throws InterruptedException {
        webDriver.findElement(By.xpath("//span[text()='Legg til informasjonsside']")).click();
        waitForPageToLoad();
        webDriver.findElement(By.xpath("//*[contains(@class, 'ui-inputfield ui-inputtext ui-widget ui-state-default ui-corner-all')]")).sendKeys("TestInfoSide");
        webDriver.findElement(By.xpath(I_FRAME)).click();
        List<WebElement> webElements = webDriver.findElements(By.xpath("//*[contains(@class, 'ui-editor-button')]"));
        webElements.get(8).click();
        webDriver.findElement(By.xpath("//*[contains(@class, 'ui-editor-popup ui-editor-list')]/div/h1[text()='Header 1']")).click();
        webDriver.findElement(By.xpath(I_FRAME)).sendKeys("Dette er en test.\n");
        webElements.get(12).click();
        webDriver.findElement(By.xpath(I_FRAME)).sendKeys("Test1.\n");
        webDriver.findElement(By.xpath(I_FRAME)).sendKeys("Test2.");
        webDriver.findElement(By.id("infoform:upload_input")).sendKeys(new File("./src/test/resources/test.png").getAbsolutePath());
        Thread.sleep(2000);
        webDriver.findElement(By.xpath("//span[text()='Lagre']")).click();
    }

    private void deleteTestInfo() {
        navigateToInfoPage();
        waitForPageToLoad();
        webDriver.findElement(By.xpath("//tr[@data-ri='5']//span[text()='Slett']")).click();
        waitForPageToLoad();
        webDriver.findElement(By.xpath("//*[contains(@class, 'ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left ui-confirmdialog-yes')]//span[text()='Ja']")).click();
        waitForPageToLoad();
    }
}