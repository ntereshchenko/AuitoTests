package org.example.LR2;
import java.time.Duration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.time.Duration;
import java.util.List;

public class FirstLab {
    private static WebDriver firefoxDriver;
    private static final String baseURL = "https://www.nmu.org.ua/ua/";
    private static final Logger log = Logger.getLogger(FirstLab.class);
    private static final long durationSeconds = 15;
    private static final Duration timeOutInSeconds = Duration.ofSeconds(durationSeconds);
    private static WebDriverWait wait;
    private static String filename;
    private static final String screenshotPath = "src/test/screenshots/Common/";
    public static WebDriver getDriver(){
        System.setProperty("webdriver.firefox.driver","src/test/resources/geckodriver.exe");
        return new FirefoxDriver();
    }
    public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception{
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile=new File(fileWithPath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }
    @BeforeClass
    public static void setUp(){
        //run driver
        log.info("Common task log");
        log.info("Starting driver");
        firefoxDriver = getDriver();
        PropertyConfigurator.configure("src/test/resources/log4j.properties");
        wait = new WebDriverWait(firefoxDriver, timeOutInSeconds);
    }

    public void preconditions(){
        //open main page
        log.info("Navigate to the main page");
        firefoxDriver.get(baseURL);
    }

    @AfterClass
    public static void tearDown(){
        log.info("Quit driver");
        firefoxDriver.quit();
    }

    @Test
    public void testHeaderExists(){
        try{
            preconditions();
            filename = "MainPage.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
            String id = "footer2";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
            //find element by ID
            List<WebElement> targetElements = firefoxDriver.findElements(By.id(id));
            if(targetElements.size() > 0){
                WebElement footer2 = targetElements.get(0);
                //verification
                Assert.assertNotNull(footer2);
            }
            else{
                log.info(String.format("Test %s \n There is no element with id %s", "testHeaderExists", id));
            }
        }
        catch(Exception e){
            log.info(String.format("Test %s \n", "testHeaderExists"));
            log.info(e.getMessage());
        }
    }

    @Test
    public void testClickOnForStudent(){
        try{
            preconditions();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a")));
            //find element by XPath
            WebElement forStudentsButton = firefoxDriver.findElement(By.xpath("/html/body/center/div[4]/div/div[1]/ul/li[4]/a"));
            //verification
            Assert.assertNotNull(forStudentsButton);
            forStudentsButton.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("but")));
            //verification page changed
            Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseURL);
            filename = "ForStudentsPage.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
            log.info(String.format("Test %s \n Successfully entered the page 'For student'", "testClickOnForStudent"));
        }
        catch(Exception e){
            log.info(String.format("Test %s \n", "testClickOnForStudent"));
            log.info(e.getMessage());
        }
    }

    @Test
    public void testSearchFieldOnForStudentsPage(){
        try {
            String studentPageURL = "content/student_life/students/";
            firefoxDriver.get(baseURL + studentPageURL);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("input")));
            //find element by TagName
            WebElement searchButton = firefoxDriver.findElement(By.tagName("input"));
            //verification
            Assert.assertNotNull(searchButton);
            //different params of searchField
            log.info(String.format("Test %s \n","testSearchFieldOnForStudentsPage"));
            log.info(String.format("Name attribute: %s\n", searchButton.getAttribute("name")) +
                    String.format("ID attribute: %s\n", searchButton.getAttribute("id")) +
                    String.format("Type attribute: %s\n", searchButton.getAttribute("id")) +
                    String.format("Value attribute: %s\n", searchButton.getAttribute("type")) +
                    String.format("Position: (%d;%d)\n", searchButton.getLocation().x, searchButton.getLocation().y) +
                    String.format("Size: %dx%d\n", searchButton.getSize().height, searchButton.getSize().width));
            searchButton.click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("gsc-i-id1")));
            log.info("Navigate to student's search page");
            //input value
            String inputValue = "I need info";
            log.info("Enter search condition");
            WebElement searchField = firefoxDriver.findElement(By.id("gsc-i-id1"));
            searchField.sendKeys(inputValue);
            filename = "ForStudentsSearchPage.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
            log.info("Execute search");
            //click enter
            searchField.sendKeys(Keys.ENTER);
            //verification text
            Assert.assertEquals(searchField.getAttribute("value"), inputValue);
            //verification page changed
            Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), studentPageURL);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".gsc-resultsbox-visible")));
            filename = "SearchResultPage.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
        }
        catch(Exception e){
            log.info(String.format("Test %s \n", "testSearchFieldOnForStudentsPage"));
            log.info(e.getMessage());
        }
    }

    @Test
    public void testSlider(){
        try {
            log.info(String.format("Test %s \n", "testSlider"));
            preconditions();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("next")));
            //find element by class name
            WebElement nextButton = firefoxDriver.findElement(By.className("next"));
            //find element by css selector
            WebElement nextButtonByCss = firefoxDriver.findElement(By.cssSelector("a.next"));
            //verification equality
            Assert.assertEquals(nextButton, nextButtonByCss);
            WebElement previousButton = firefoxDriver.findElement(By.className("prev"));
            for (int i = 0; i < 20; i++) {
                filename = Integer.toString(i).concat(".png");
                //change count of iterations
                if (nextButton.getAttribute("class").contains("disabled")) {
                    previousButton.click();
                    takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
                    log.info("Going backwards");
                } else {
                    nextButton.click();
                    takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
                    log.info("Going forward");
                }
            }
            Assert.assertFalse(nextButton.getAttribute("class").contains("disabled"));
            Assert.assertFalse(previousButton.getAttribute("class").contains("disabled"));
            log.info("Stopped at the penultimate image");
        }
        catch(Exception e){
            log.info(String.format("Test %s \n", "testSlider"));
            log.info(e.getMessage());
        }
    }

}