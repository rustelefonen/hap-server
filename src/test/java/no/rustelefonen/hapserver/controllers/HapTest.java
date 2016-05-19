package no.rustelefonen.hapserver.controllers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Simen Fonnes on 09.05.2016.
 */
public class HapTest {

    static WebDriver webDriver;
    private static final String WEB_BROWSER_NAME = "firefox";
    private static final String HAP_ROOT = "http://localhost:8080";

    @BeforeClass
    public static void init() {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.htmlUnit();
        desiredCapabilities.setBrowserName(WEB_BROWSER_NAME);
        desiredCapabilities.setJavascriptEnabled(true);
        webDriver = new FirefoxDriver(desiredCapabilities);
    }

    @AfterClass
    public static void tearDown() {
        webDriver.close();
    }

    @Before
    public void login() throws InterruptedException {
        webDriver.get(HAP_ROOT);
        waitForPageToLoad();
        webDriver.findElement(By.id("j_idt15:j_idt20")).clear();
        webDriver.findElement(By.id("j_idt15:j_idt20")).sendKeys("admin");
        webDriver.findElement(By.id("j_idt15:j_idt22")).clear();
        webDriver.findElement(By.id("j_idt15:j_idt22")).sendKeys("admin");
        webDriver.findElement(By.xpath("//*[contains(@class,'ui-button-text ui-c')]")).click();
        waitForPageToLoad();
    }

    @After
    public void logout() {
        waitForPageToLoad();
        webDriver.findElement(By.xpath("//span[text()='Logg ut']")).click();
    }

    //https://github.com/arcuri82/pg6100/blob/master/ejb_selenium_example/src/test/java/org/pg6100/seleniumexample/CounterPageObject.java
    Boolean waitForPageToLoad() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
        WebDriverWait wait = new WebDriverWait(webDriver, 10); //give up after 10 seconds

        //keep executing the given JS till it returns "true", when page is fully loaded and ready
        return wait.until((ExpectedCondition<Boolean>) input -> {
            String res = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(res);
        });
    }
}
