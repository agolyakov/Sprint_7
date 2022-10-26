import api.client.OrdersClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    @Test
    @DisplayName("Get order list and check orders")
    public void testGetOrderListAndCheckResponseBody() {
        OrdersClient ordersClient = new OrdersClient();
        Response testGetOrderListAndCheckResponseBody = ordersClient.getResponseGet();

        testGetOrderListAndCheckResponseBody
                .then()
                .assertThat()
                .body("orders", notNullValue())
                .statusCode(200);
    }

}
