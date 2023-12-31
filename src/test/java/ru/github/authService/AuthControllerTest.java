package ru.github.authService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.github.authService.util.JwtTokenUtil;
import ru.github.authService.controller.AuthController;
import ru.github.authService.to.RefreshJwtRequest;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.github.authService.TestData.*;

public class AuthControllerTest extends AbstractControllerTest {
    @Autowired
    private JwtTokenUtil tokenUtil;

    public AuthControllerTest() {
        super(AuthController.URL);
    }

    //---------------------------------------------------------
    @Test
    public void requestToProtected_accessTokenExpired() throws Exception {
        post("/refresh", status().isForbidden(), null, CLIENT1_EXPIRED_ACCESS_TOKEN)
                .andExpect(content().string(containsString("JWT expired")));
    }

    @Test
    public void requestToProtected_accessTokenNotValid() throws Exception {
        post("/refresh", status().isForbidden(), null, CLIENT1_NOT_VALID_ACCESS_TOKEN)
                .andExpect(content().string(containsString("JWT signature does not match locally computed signature.")));
    }

    //-------------------AUTHENTICATION-----------------------
    @Test
    @WithUserDetails(value = CLIENT1_LOGIN)
    public void authenticateClient_success() throws Exception {
        String jsonResponse = getContent(post("/signin", status().isOk()));

        tokenUtil.validateAccess(getJsonParam(jsonResponse, "accessToken"));
        tokenUtil.validateRefresh(getJsonParam(jsonResponse, "refreshToken"));
    }

    //-------------------REFRESHING_ACCESS_TOKEN-----------------------
    @Test
    @WithUserDetails(value = CLIENT1_LOGIN)
    public void getNewAccessToken_success() throws Exception {
        String authorization = getContent(post("/signin", status().isOk()));
        String refreshToken = getJsonParam(authorization, "refreshToken");

        String newAuthorization = getContent(post("/token", status().isOk(), new RefreshJwtRequest(refreshToken)));
        tokenUtil.validateAccess(getJsonParam(newAuthorization, "accessToken"));
        String newRefresh = getJsonParam(newAuthorization, "refreshToken");
        tokenUtil.validateRefresh(newRefresh);
        assertEquals(refreshToken, newRefresh);
    }

    @Test
    public void getNewAccessToken_notStoredRefreshToken() throws Exception {
        String notStoredRefreshToken = tokenUtil.generateRefresh(CLIENT3);
        post("/token", status().isForbidden(), new RefreshJwtRequest(notStoredRefreshToken));
    }

    @Test
    public void getNewAccessToken_refreshExpired() throws Exception {
        post("/token", status().isForbidden(), new RefreshJwtRequest(CLIENT1_EXPIRED_REFRESH_TOKEN))
                .andExpect(content().string(containsString("JWT expired")));
    }

    @Test
    public void getNewAccessToken_refreshNotValid() throws Exception {
        post("/token", status().isForbidden(), new RefreshJwtRequest(CLIENT1_NOT_VALID_REFRESH_TOKEN))
                .andExpect(content().string(containsString("JWT signature does not match locally computed signature.")));
    }

    //-------------------REFRESHING_REFRESH_TOKEN-----------------------
    @Test
    @WithUserDetails(value = CLIENT1_LOGIN)
    public void refreshTokens_success() throws Exception {
        String authorization = getContent(post("/signin", status().isOk()));
        String refreshToken = getJsonParam(authorization, "refreshToken");
        String accessToken = getJsonParam(authorization, "accessToken");

        String refreshedTokens = getContent(post("/refresh", status().isOk(), new RefreshJwtRequest(refreshToken), accessToken));

        tokenUtil.validateRefresh(getJsonParam(refreshedTokens, "refreshToken"));
        tokenUtil.validateAccess(getJsonParam(authorization, "accessToken"));
    }

    @Test
    public void refreshTokens_notStoredRefreshToken() throws Exception {
        String notStoredRefreshToken = tokenUtil.generateRefresh(CLIENT3);
        post("/refresh", status().isForbidden(), new RefreshJwtRequest(notStoredRefreshToken), tokenUtil.generateAccess(CLIENT2));
    }

    @Test
    public void refreshTokens_refreshExpired() throws Exception {
        post("/refresh", status().isForbidden(), new RefreshJwtRequest(CLIENT1_EXPIRED_REFRESH_TOKEN), tokenUtil.generateAccess(CLIENT1))
                .andExpect(content().string(containsString("JWT expired")));
    }

    @Test
    public void refreshTokens_refreshNotValid() throws Exception {
        post("/refresh", status().isForbidden(), new RefreshJwtRequest(CLIENT1_NOT_VALID_REFRESH_TOKEN), tokenUtil.generateAccess(CLIENT1))
                .andExpect(content().string(containsString("JWT signature does not match locally computed signature.")));
    }

    //-------------------SIGNOUT--------------------------
    @Test
    @WithUserDetails(value = CLIENT1_LOGIN)
    public void signout_success() throws Exception {
        String authorization = getContent(post("/signin", status().isOk()));
        String accessToken = getJsonParam(authorization, "accessToken");
        String refreshToken = getJsonParam(authorization, "refreshToken");

        post("/signout", status().isOk(), null, accessToken);

        post("/refresh", status().isForbidden(), new RefreshJwtRequest(refreshToken), accessToken)
                .andExpect(content().string(containsString("Refresh token not found")));
    }
}
