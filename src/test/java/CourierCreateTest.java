import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

//курьера можно создать;
//нельзя создать двух одинаковых курьеров;
//чтобы создать курьера, нужно передать в ручку все обязательные поля;
//запрос возвращает правильный код ответа;
//успешный запрос возвращает ok: true;
//если одного из полей нет, запрос возвращает ошибку;
//если создать пользователя с логином, который уже есть, возвращается ошибка.

public class CourierCreateTest {

    @Test
    @DisplayName("Check status ok with correct create courier")
    public void testOkMessageForCreateCourier() {
        CourierClient courierClient = new CourierClient();
        Response testOkMessageForCreateCourier =
                courierClient.getResponse("create", new CourierClient().getJsonBody("correctCourier"));

        testOkMessageForCreateCourier
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Check error message when create two same courier")
    public void testErrorMessageForCreateTwoSameCourier() {
        CourierClient courierClient = new CourierClient();
        Response createFirstCourier =
                courierClient.getResponse("create", new CourierClient().getJsonBody("correctCourier"));

        createFirstCourier
                .then()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", is(true));


        Response testErrorMessageForCreateTwoSameCourier =
                courierClient.getResponse("create", new CourierClient().getJsonBody("correctCourier"));

        testErrorMessageForCreateTwoSameCourier
                .then()
                .statusCode(409)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Check error message when create courier without variable 'login'")
    public void testErrorMessageForCreateCourierWithoutLogin() {
        CourierClient courierClient = new CourierClient();
        Response testErrorMessageForCreateCourierWithoutLogin =
                courierClient.getResponse("create", new CourierClient().getJsonBody("noLogin"));

        testErrorMessageForCreateCourierWithoutLogin
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Check error message when create courier without variable 'password'")
    public void testErrorMessageForCreateCourierWithoutPass() {
        CourierClient courierClient = new CourierClient();
        Response testErrorMessageForCreateCourierWithoutPass =
                courierClient.getResponse("create", new CourierClient().getJsonBody("noPass"));

        testErrorMessageForCreateCourierWithoutPass
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));

    }

    @After
    public void deleteCourierIfIdIsNotNull() {
        CourierClient courierClient = new CourierClient();
        courierClient.deleteCourierIfIdIsNotNull();
    }

}
