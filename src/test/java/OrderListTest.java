import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void getOrderListAndCheckResponseBody() {
        Response response = given()
                .when()
                .get("/api/v1/orders");
        response.then().assertThat().body("orders", notNullValue()).statusCode(200);
    }

}
