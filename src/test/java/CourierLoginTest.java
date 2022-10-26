import api.client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

//курьер может авторизоваться;
//для авторизации нужно передать все обязательные поля;
//система вернёт ошибку, если неправильно указать логин или пароль;
//если какого-то поля нет, запрос возвращает ошибку;
//если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;
//успешный запрос возвращает id.

public class CourierLoginTest {

    @Before
    public void createCourier() {
        CourierClient courierClient = new CourierClient();
        courierClient.getResponse("create", new CourierClient().getJsonBody("correctCourier"));
    }

    @Test
    @DisplayName("Check isNotNull id with correct login")
    public void testOKStatusAndReturnNotNullIdWithCorrectAuth() {
        CourierClient courierClient = new CourierClient();
        Response testOKStatusAndReturnNotNullIdWithCorrectAuth =
                courierClient.getResponse("login", new CourierClient().getJsonBody("correctCourier"));

        testOKStatusAndReturnNotNullIdWithCorrectAuth
                .then()
                .statusCode(200)
                .and()
                .assertThat()
                .body("id", notNullValue());
    }

    //Тест падает из-за несоответствия требований
    //При отсутствии в запросе поля password сервис возвращает 504 ошибку вместо ожидаемой 400
    @Test
    @DisplayName("Check error message with login without variable 'password'")
    public void testErrorMessageForAuthWithoutPass() {
        CourierClient courierClient = new CourierClient();
        Response testErrorMessageForAuthWithoutPass =
                courierClient.getResponse("login", new CourierClient().getJsonBody("noPass"));

        testErrorMessageForAuthWithoutPass
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check error message with login without variable 'login'")
    public void testErrorMessageForAuthWithoutLogin() {
        CourierClient courierClient = new CourierClient();
        Response testErrorMessageForAuthWithoutLogin =
                courierClient.getResponse("login", new CourierClient().getJsonBody("noLogin"));

        testErrorMessageForAuthWithoutLogin
                .then()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check error message with login not created courier")
    public void testErrorMessageForAuthNotCreatedCourier() {
        CourierClient courierClient = new CourierClient();
        Response testErrorMessageForLoginNotCreatedCourier =
                courierClient.getResponse("login", new CourierClient().getJsonBody("notCreated"));

        testErrorMessageForLoginNotCreatedCourier
                .then()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check error message for incorrect password")
    public void testErrorMessageForIncorrectPassword() {
        CourierClient courierClient = new CourierClient();
        Response testErrorMessageForIncorrectPassword =
                courierClient.getResponse("login", new CourierClient().getJsonBody("incorrectPass"));

        testErrorMessageForIncorrectPassword
                .then()
                .statusCode(404)
                .and()
                .assertThat()
                .body("message", is("Учетная запись не найдена"));
    }

    @After
    public void deleteCourierIfIdIsNotNull() {
        CourierClient courierClient = new CourierClient();
        courierClient.deleteCourierIfIdIsNotNull();
    }

}
