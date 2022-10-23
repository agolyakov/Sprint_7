import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//курьер может авторизоваться;
//для авторизации нужно передать все обязательные поля;
//система вернёт ошибку, если неправильно указать логин или пароль;
//если какого-то поля нет, запрос возвращает ошибку;
//если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
//успешный запрос возвращает id.

public class CourierLoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";

        File json = new File("src/test/resources/courier.json");
        given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    @Test
    public void authWithAllParams() {
        File json = new File("src/test/resources/courier.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue()).statusCode(200);
    }

    //Тест падает из-за несоответствия требований
    //При отсутствии в запросе поля password сервис возвращает 504 ошибку вместо ожидаемой 400
    @Test
    public void authWithoutPass() {
        File json = new File("src/test/resources/courierWithoutPass.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).statusCode(400);
    }

    @Test
    public void authWithoutLogin() {
        File json = new File("src/test/resources/courierWithoutLogin.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).statusCode(400);
    }

    @Test
    public void authNoCreatedCourier() {
        File json = new File("src/test/resources/courierNoCreated.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
    }

    @Test
    public void authWithBadPass() {
        File json = new File("src/test/resources/courierWithBadPass.json");
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
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
