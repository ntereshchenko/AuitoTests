package org.example.LR3;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class IndividualTask {
    private static WebDriver firefoxDriver;
    private static final Logger log = Logger.getLogger(UserTest.class);
    private static final String baseURL = "https://petstore.swagger.io/v2/";
    private static final String PET = "/pet",
            PET_ID = PET + "/{petId}",
            PET_STATUS = PET + "/findByStatus",
            PET_UPLOAD_IMAGE = PET_ID + "/uploadImage";
    private static int petId;
    private static String petName;
    private static final List<String> statusList = List.of("available", "pending", "sold");
    private static String status;

    public static WebDriver getDriver(){
        System.setProperty("webdriver.firefox.driver","src/test/resources/geckodriver.exe");
        return new FirefoxDriver();
    }
    @BeforeClass
    public void setup(){
        try{
            log.info("----------------------------------------------");
            log.info("Individual task log");
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
    @Test()
    public void verifyAddPetAction(){
        try{
            log.info("Trying to add a new pet");
            petId = ThreadLocalRandom.current().nextInt(1, 10 + 1);
            petName = Faker.instance().name().name();
            Map<String,?> category = Map.of(
                    "id",ThreadLocalRandom.current().nextInt(1, 10 + 1),
                    "name",Faker.instance().lordOfTheRings().character()
            );
            Map<String,?> tag = Map.of(
                    "id",ThreadLocalRandom.current().nextInt(1, 10 + 1),
                    "name",Faker.instance().lordOfTheRings().character()
            );
            String[] URLs = {"https://picsum.photos/seed/picsum/200/300"};
            ArrayList<Map<String, ?>> tags = new ArrayList<>();
            tags.add(tag);
            status = statusList.get(new Random().nextInt(statusList.size()));
            Map<String,?> body = Map.of(
                    "id", petId,
                    "category", category,
                    "name", petName,
                    "photoUrls",URLs,
                    "tags", tags.toArray(),
                    "Status",status
            );
            given().body(body)
                    .post(PET)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyAddPetAction")
    public void verifyUpdatePetAction(){
        try{
            log.info("Trying to update a pet");
            Map<String,?> category = Map.of(
                    "id",ThreadLocalRandom.current().nextInt(1, 10 + 1),
                    "name",Faker.instance().lordOfTheRings().character()
            );
            Map<String,?> tag = Map.of(
                    "id",ThreadLocalRandom.current().nextInt(1, 10 + 1),
                    "name",Faker.instance().lordOfTheRings().character()
            );
            String[] URLs = {"https://picsum.photos/seed/picsum/200/300"};
            ArrayList<Map<String, ?>> tags = new ArrayList<>();
            tags.add(tag);
            status = statusList.get(new Random().nextInt(statusList.size()));
            Map<String,?> body = Map.of(
                    "id", petId,
                    "category", category,
                    "name", petName,
                    "photoUrls",URLs,
                    "tags", tags.toArray(),
                    "Status",status
            );
            given().body(body)
                    .put(PET)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyAddPetAction")
    public void verifyUpdatePetByIdAction(){
        try{
            log.info("Trying to update a pet by Id");
            petName = Faker.instance().name().name();
            status = statusList.get(new Random().nextInt(statusList.size()));
            Map<String,?> body = Map.of(
                    "name", petName,
                    "status", status
            );
            given()
                    .contentType("application/x-www-form-urlencoded")
                    .accept("application/json")
                    .pathParam("petId", petId)
                    .formParams(body)
                    .post(PET_ID)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyAddPetAction")
    public void verifyUploadPetImageAction(){
        try{
            log.info("Trying to upload a pet image");
            petName = Faker.instance().name().name();
            status = statusList.get(new Random().nextInt(statusList.size()));
            File newImage = new File("src/test/resources/german_shepherd.png");
            given()
                    .contentType("multipart/form-data")
                    .accept("application/json")
                    .pathParam("petId", petId)
                    .multiPart("file",newImage)
                    .formParam("additionalMetadata", "")
                    .post(PET_UPLOAD_IMAGE)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyAddPetAction")
    public void verifyFindPetByIdAction(){
        try{
            log.info("Trying to find added pet by id");
            given().pathParam("petId", petId)
                    .get(PET_ID)
                    .then()
                    .statusCode(HttpStatus.SC_OK)
                    .and()
                    .body("name", equalTo(petName));
        }catch(Exception e) {
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyAddPetAction")
    public void verifyFindPetsByStatusAction(){
        try{
            log.info("Trying to find added pet by status");
            given()
                    .queryParam("status", status)
                    .get(PET_STATUS)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e) {
            log.error(e.getMessage());
        }
    }
    @Test(dependsOnMethods = "verifyAddPetAction", priority = 1)
    public void verifyDeletePetAction(){
        try{
            log.info("Trying to delete added pet");
            given().pathParam("petId", petId)
                    .delete(PET_ID)
                    .then()
                    .statusCode(HttpStatus.SC_OK);
        }catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}
