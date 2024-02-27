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

public class Indiv {
    private static WebDriver firefoxDriver;
    private static final String baseURL = "https://planetakino.ua/";
    private static final Logger log = Logger.getLogger(FirstLab.class);
    private static final long durationSeconds = 15;
    private static final Duration timeOutInSeconds = Duration.ofSeconds(durationSeconds);
    private static WebDriverWait wait;
    private static String filename;
    private static final String screenshotPath = "src/test/screenshots/Indiv/";
    public static WebDriver getDriver(){
        System.setProperty("webdriver.firefox.driver","src/test/resources/geckodriver.exe");
       // System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");

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
    public void testClickOnShowtimes(){
        try{
            preconditions();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[1]/header/div/div[1]/ul/li[1]/a")));
            //find element by XPath
            WebElement ShowtimesButton = firefoxDriver.findElement(By.xpath("/html/body/div[1]/header/div/div[1]/ul/li[1]/a"));
            //verification
            Assert.assertNotNull(ShowtimesButton );
            ShowtimesButton .click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("but")));
            //verification page changed
            Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), baseURL);
            filename = "Showtimes.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
            log.info(String.format("Test %s \n Successfully entered the page 'ShowtimesButton '", "testClickOnShowtimes"));
        }
        catch(Exception e){
            log.info(String.format("Test %s \n", "testClickOnForStudent"));
            log.info(e.getMessage());
        }
    }
      /*  @Test
        public void testSearchFieldOnMoviesPage(){
            try {
                String moviestPageURL = "/dnipro/movies/";
                firefoxDriver.get(baseURL + moviestPageURL);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("input")));
                //find element by TagName
                WebElement searchButton = firefoxDriver.findElement(By.tagName("input"));
                //verification
                Assert.assertNotNull(searchButton);
                //different params of searchField
                log.info(String.format("Test %s \n","testSearchFieldOnMoviesPage"));
                log.info(String.format("Class attribute: %s\n", searchButton.getAttribute("class")) +
                        String.format("Type attribute: %s\n", searchButton.getAttribute("id"));
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
                Assert.assertNotEquals(firefoxDriver.getCurrentUrl(), moviestPageURL);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".gsc-resultsbox-visible")));
                filename = "SearchResultPage.png";
                takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
            }
            catch(Exception e){
                log.info(String.format("Test %s \n", "testSearchFieldOnForStudentsPage"));
                log.info(e.getMessage());
            }
        }*/
    @Test
    public void testSlider() {
        try {
            log.info(String.format("Test %s \n", "testSlider"));
            preconditions();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.slider-block__carousel-control:nth-child(2)")));

            // find elements by css selector
            WebElement nextButton = firefoxDriver.findElement(By.cssSelector("a.slider-block__carousel-control:nth-child(2)"));
            WebElement previousButton = firefoxDriver.findElement(By.cssSelector("a.slider-block__carousel-control-left"));

            for (int i = 0; i < 20; i++) {
                filename = Integer.toString(i).concat(".png");
                // change count of iterations
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
        } catch (Exception e) {
            log.info(String.format("Test %s \n", "testSlider"));
            log.info(e.getMessage());
        }
    }

}
