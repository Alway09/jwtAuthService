package ru.github.authService;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.github.authService.controller.AuthController;
import ru.github.authService.controller.ClientController;
import ru.github.authService.to.ClientChangePasswordTo;
import ru.github.authService.to.ClientTo;
import ru.github.authService.to.ClientUpdateInfoTo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.github.authService.TestData.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientControllerTest extends AbstractControllerTest {

    public ClientControllerTest() {
        super(ClientController.URL);
    }

    //---------------------------------------------------------
    @Test
    public void request_clientTo_notValid() throws Exception {
        post("/signup", status().isUnprocessableEntity(), new ClientTo("Aa", "2234"));
    }

    @Test
    public void request_clientTo_duplicatedLogin() throws Exception {
        post("/signup", status().isUnprocessableEntity(), new ClientTo(CLIENT1.getLogin(), "123456789"));
    }

    //--------------------REGISTRATION------------------------
    @Test
    @Order(1)
    public void register_success() throws Exception {
        post("/signup", status().isCreated(), CLIENT_NEW_TO);
        postWithBasicAuth(AuthController.URL + "/signin", status().isOk(), CLIENT_NEW_TO);
    }

    @Test
    public void register_notValid() throws Exception {
        post("/signup", status().isUnprocessableEntity(), new ClientTo("s", "a"));
    }

    //--------------------DISABLE------------------------
    @Test
    @Order(2)
    public void disable_success() throws Exception {
        String accessToken = getAccessToken(CLIENT_NEW_TO);
        patch("/disable", status().isOk(), null, accessToken);
    }

    //--------------------ENABLE-------------------------
    @Test
    @Order(3)
    public void enable_success() throws Exception {
        patchWithBasicAuth("/enable", status().isOk(), CLIENT_NEW_TO);
    }

    //--------------------UPDATE------------------------
    @Test
    @Order(4)
    public void update_success() throws Exception {
        String accessToken = getAccessToken(CLIENT_NEW_TO);
        put("/update", status().isOk(), new ClientUpdateInfoTo("string123"), accessToken);
    }

    @Test
    public void update_duplicateLogin() throws Exception {
        String accessToken = getAccessToken(CLIENT1_TO);
        put("/update", status().isUnprocessableEntity(), new ClientUpdateInfoTo(CLIENT2.getLogin()), accessToken);
    }

    @Test
    public void update_notValid() throws Exception {
        String accessToken = getAccessToken(CLIENT1_TO);
        put("/update", status().isUnprocessableEntity(), new ClientUpdateInfoTo("A"), accessToken);
    }

    //--------------------CHANGE PASSWORD------------------------
    @Test
    @Order(5)
    public void changePassword_success() throws Exception {
        String accessToken = getAccessToken(CLIENT2_TO);
        put("/changePass", status().isOk(), new ClientChangePasswordTo("newnewPassPass"), accessToken);
    }

    @Test
    public void changePassword_notValid() throws Exception {
        String accessToken = getAccessToken(CLIENT1_TO);
        put("/changePass", status().isUnprocessableEntity(), new ClientChangePasswordTo("e"), accessToken);
    }
}
