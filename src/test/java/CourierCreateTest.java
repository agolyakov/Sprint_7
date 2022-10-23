import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//курьера можно создать;
//нельзя создать двух одинаковых курьеров;
//чтобы создать курьера, нужно передать в ручку все обязательные поля;
//запрос возвращает правильный код ответа;
//успешный запрос возвращает ok: true;
//если одного из полей нет, запрос возвращает ошибку;
//если создать пользователя с логином, который уже есть, возвращается ошибка.

public class CourierCreateTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void createCourierWithAllParams() {
        File json = new File("src/test/resources/courier.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    @Test
    public void createTwoSameCourier() {
        File json = new File("src/test/resources/courier.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);

        Response response1 = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
        response1.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);
    }

    @Test
    public void createCourierWithoutLogin() {
        File json = new File("src/test/resources/courierWithoutLogin.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    public void createCourierWithoutPass() {
        File json = new File("src/test/resources/courierWithoutPass.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @After
    public void getCourierIdAndDeleteCourier() {
        File json = new File("src/test/resources/courier.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");

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

}
