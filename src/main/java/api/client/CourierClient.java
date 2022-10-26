package api.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Get response body")
    public Response getResponse(String url, File body) {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

        switch (url) {
            case "login": url = "/api/v1/courier/login";
            break;
            case "create": url = "api/v1/courier";
            break;
            default: url = "";
            break;
        }

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(body)
                .when()
                .post(url);
    }

    @Step("Login and check status code, if == 200 get id courier and delete")
    public void deleteCourierIfIdIsNotNull() {
        Response response = getResponse("login", getJsonBody("correctCourier"));

        if (response.getStatusCode() == 200) {
            JsonPath jsonPath = new JsonPath(response.body().asString());
            int id = jsonPath.get("id");

            given()
                    .header("Content-Type", "application/json")
                    .and()
                    .body(response.body().asString())
                    .when()
                    .delete("/api/v1/courier/" + id);
        }
    }

    public File getJsonBody(String json) {
        String path;

        switch (json) {
            case "correctCourier": path = "src/test/resources/courier.json";
            break;
            case "incorrectPass": path = "src/test/resources/courierWithBadPass.json";
            break;
            case "incorrectLogin": path = "src/test/resources/courierWithBadLogin.json";
            break;
            case "notCreated": path = "src/test/resources/courierNotCreated.json";
            break;
            case "noLogin": path = "src/test/resources/courierWithoutLogin.json";
            break;
            case "noPass": path = "src/test/resources/courierWithoutPass.json";
            break;
            default: path = "";
            break;
        }

        return new File(path);
    }




}
