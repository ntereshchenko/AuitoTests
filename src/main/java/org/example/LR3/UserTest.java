package org.example.LR3;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserTest {
    private static WebDriver firefoxDriver;
    private static final Logger log = Logger.getLogger(UserTest.class);
    private static final String baseURL = "https://petstore.swagger.io/v2/";
    private static final String USER = "/user",
            USER_USERNAME = USER + "/{username}",
            USER_LOGIN = USER + "/login",
            USER_LOGOUT = USER + "/logout";
    private String username;
    private String firstName;
    public static WebDriver getDriver(){
        System.setProperty("webdriver.firefox.driver","src/test/resources/geckodriver.exe");
        return new FirefoxDriver();
    }
    @BeforeClass
    public void setup(){
        try{
            log.info("----------------------------------------------");
            log.info("Common task log");
            log.info("Starting driver");
            firefoxDriver = getDriver();
            PropertyConfigurator.configure("src/test/resources/log4j.properties");
            RestAssured.baseURI = baseURL;
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
            RestAssured.responseSpecification = new ResponseSpecBuilder().build();
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @AfterClass
    public static void afterEverything()
    {
        try{
            log.info("Closing web driver");
            if(firefoxDriver != null)
            {
                firefoxDriver.close();
            }
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Test
    public void verifyLoginAction(){
        try{
            log.info("Trying to login with credentials AnastasiaTereshchenko / 122-21sk-1.12");
            Map<String,?> body = Map.of(
                    "username","AnastasiaTereshchenko",
                    "password","122-21sk-1.12"
            );
            Response response = given().body(body).get(USER_LOGIN);
            response.then().statusCode(HttpStatus.SC_OK);
            RestAssured.requestSpecification.sessionId(response.jsonPath()
                    .get("message")
                    .toString()
                    .replaceAll("[^0-9]",""));
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyLoginAction")
    public void verifyCreateAction(){
        try{
            log.info("Trying to sign in a new user");
            username = Faker.instance().name().username();
            firstName = Faker.instance().lordOfTheRings().character();
            Map<String,?> body = Map.of(
                    "username", username,
                    "firstName", firstName,
                    "lastName",Faker.instance().lordOfTheRings().character(),
                    "email",Faker.instance().internet().emailAddress(),
                    "password",Faker.instance().internet().password(),
                    "phone",Faker.instance().phoneNumber().phoneNumber(),
                    "userStatus",Integer.valueOf("1")
            );
            given().body(body)
                    .post(USER)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyCreateAction")
    public void verifyGetAction(){
        try{
            log.info("Trying to acquire created user data");
            given().pathParam("username",username)
                    .get(USER_USERNAME)
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .and()
                    .body("firstName", equalTo(firstName));
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyGetAction")
    public void verifyDeleteAction(){
        try{
            log.info("Trying to delete created user");
            given().pathParam("username", username)
                    .delete(USER_USERNAME)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e) {
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyLoginAction", priority = 1)
    public void verifyLogoutAction(){
        try{
            log.info("Trying to logout");
            given().get(USER_LOGOUT)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
}
