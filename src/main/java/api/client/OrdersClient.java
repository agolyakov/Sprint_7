package api.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrdersClient {

    private static final String orders = "/api/v1/orders";

    public Response getResponsePost(Object object) {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

        return  given()
                .header("Content-Type", "application/json")
                .and()
                .body(object)
                .when()
                .post(orders);
    }

    public Response getResponseGet() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

        return given()
                .when()
                .get(orders);

    }

}
