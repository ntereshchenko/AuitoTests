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
    public void testLogoElementExists() {
        try {
            preconditions();
            filename = "MainPage.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));

            // Унікальний ідентифікатор елемента
            String logoId = "logo";

            // Очікування видимості елемента за ідентифікатором
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(logoId)));

            // Знаходження елементу за ідентифікатором
            WebElement logoElement = firefoxDriver.findElement(By.id(logoId));

            // Перевірка наявності елементу
            Assert.assertNotNull(logoElement);

            log.info(String.format("Test %s \n", "testLogoElementExists"));
            log.info(String.format("Logo element with id %s exists on the page", logoId));

            // Виконання інших дій з елементом, які вам потрібні

            filename = "LogoElementExists.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
        } catch (Exception e) {
            log.info(String.format("Test %s \n", "testLogoElementExists"));
            log.info(e.getMessage());
        }
    }

    @Test
    public void testClickOnElement() {
        try {
            preconditions();

            // Змінено знаходження елементу за CSS-селектор
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("li.h-m__item:nth-child(1) > a:nth-child(1)")));

            // Змінено знаходження елементу за CSS-селектор
            WebElement elementToClick = firefoxDriver.findElement(By.cssSelector("li.h-m__item:nth-child(1) > a:nth-child(1)"));

            // Перевірка наявності елементу
            Assert.assertNotNull(elementToClick);

            // Клік по елементу
            elementToClick.click();

            // Очікування на те, щоб з'явився якийсь елемент після кліку
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".some-other-element")));

            // Змінено ім'я файлу для скріншоту після кліку
            filename = "Showtimes_Clicked.png";

            // Затримка перед зніманням скріншоту, щоб дати час для завантаження нового вмісту
            Thread.sleep(2000);

            // Зняти скріншот
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));

            // Логування успішного кліку
            log.info(String.format("Test %s \n Successfully clicked on the element", "testClickOnElement"));
        } catch (Exception e) {
            log.info(String.format("Test %s \n", "testClickOnElement"));
            log.info(e.getMessage());
        }
    }




    @Test
    public void testInputDataAndCheckField() {
        try {
            preconditions();
            filename = "MainPage.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));

            // Знаходження елементу за CSS
            WebElement iconElement = firefoxDriver.findElement(By.cssSelector("i.material-icons:nth-child(2)"));

            // Перевірка наявності елементу
            Assert.assertNotNull(iconElement);

            // Натискання на іконку
            iconElement.click();

            // Знаходження елементу за CSS
            WebElement searchInput = firefoxDriver.findElement(By.cssSelector(".search-input"));

            // Перевірка наявності елементу
            Assert.assertNotNull(searchInput);

            // Введення даних у поле
            String inputData = "Вонка";
            searchInput.sendKeys(inputData);

            // Перевірка, чи є дані у полі
            Assert.assertEquals(searchInput.getAttribute("value"), inputData);

            log.info(String.format("Test %s \n", "testInputDataAndCheckField"));
            log.info("Clicked on the icon to activate the search field");
            log.info(String.format("Input data '%s' into the search field", inputData));
            log.info(String.format("Search field contains data: %s", searchInput.getAttribute("value")));

            // Виконання інших дій з елементом, які вам потрібні

            filename = "InputDataAndCheckField.png";
            takeSnapShot(firefoxDriver, screenshotPath.concat(filename));
        } catch (Exception e) {
            log.info(String.format("Test %s \n", "testInputDataAndCheckField"));
            log.info(e.getMessage());
        }
    }


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
