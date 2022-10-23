import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//можно указать один из цветов — BLACK или GREY;
//можно указать оба цвета;
//можно совсем не указывать цвет;
//тело ответа содержит track.

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final Integer rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;
    private final Integer expected;

    public OrderCreateTest(String firstName, String lastName, String address, String metroStation, String phone,
                     Integer rentTime, String deliveryDate, String comment, List<String> color, Integer expected) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[] getOrderData() {
        return new Object[][]{
                {"Александр", "Голяков", "г. Нижний Новгород", "Горьковская", "8 800 555 35 35", 4, "2022-11-23",
                        "sasha, go hard", Collections.singletonList("BLACK"), 201},
                {"Александр", "Голяков", "г. Нижний Новгород", "Горьковская", "8 800 555 35 35", 4, "2022-11-23",
                        "sasha, go hard", Collections.singletonList("GREY"), 201},
                {"Александр", "Голяков", "г. Нижний Новгород", "Горьковская", "8 800 555 35 35", 4, "2022-11-23",
                        "sasha, go hard", Collections.singletonList("BLACK, GREY"), 201},
                {"Александр", "Голяков", "г. Нижний Новгород", "Горьковская", "8 800 555 35 35", 4, "2022-11-23",
                        "sasha, go hard", Collections.singletonList(""), 201}

        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void checkOrderCreateWithParameters() {

        OrderData orderData = new OrderData(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate,
                comment, color);
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(orderData)
                .when()
                .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue()).and().statusCode(expected);
    }

}
