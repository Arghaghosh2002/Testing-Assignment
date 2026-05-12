package tests;

import io.restassured.RestAssured;
import mockserver.WeatherMockServer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.internal.EclipseInterface;

public class WeatherApiTest {

    private WeatherMockServer mockServer;

    @BeforeMethod
    public void setUp() throws Exception {
        mockServer = new WeatherMockServer();
        mockServer.start();
    }

    @Test
    public void testWeatherApiResponse() {

        Response response = RestAssured
                .given()
                .baseUri("http://localhost:8080")
                .header("Accept", "application/json")
                .when()
                .get("/api/weather")
                .then()
                .extract()
                .response();

        //System.out.println("Status Code   : " + response.getStatusCode());
        //System.out.println("Response Body : " + response.getBody().asString());

        Assert.assertEquals(
                response.getStatusCode(),
                200,
                "Expected HTTP 200 but got: " + response.getStatusCode()
        );
        //System.out.println("PASS — Status code is 200.");

        String city = response.jsonPath().getString("city");
        Assert.assertEquals(
                city,
                "Kolkata",
                "City mismatch. Expected: Kolkata, Got: " + city
        );
        //System.out.println("PASS — city       : " + city);

        int temperature = response.jsonPath().getInt("temperature");
        Assert.assertEquals(
                temperature,
                30,
                "Temperature mismatch. Expected: 30, Got: " + temperature
        );
        //System.out.println("PASS — temperature: " + temperature);

        String condition = response.jsonPath().getString("condition");
        Assert.assertEquals(
                condition,
                "Humid",
                "Condition mismatch. Expected: Humid, Got: " + condition
        );
        //System.out.println("PASS — condition  : " + condition);
    }

    @AfterMethod
    public void tearDown() {
        mockServer.stop();
    }
}