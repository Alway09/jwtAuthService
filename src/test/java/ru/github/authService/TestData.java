package ru.github.authService;

import ru.github.authService.model.Client;
import ru.github.authService.to.ClientTo;

public class TestData {
    public static final Integer CLIENT1_ID = 1;
    public static final Integer CLIENT2_ID = 2;
    public static final Integer CLIENT3_ID = 3;
    public static final Integer CLIENT_NEW_ID = 4;

    public static final Client CLIENT1 = new Client(CLIENT1_ID, "valentine", "{bcrypt}$2a$10$SaDBEsf8xdO9YBu/g27Zde2C5xDVePV.HX3a1aihq958WtwCL0B1S");
    public static final Client CLIENT2 = new Client(CLIENT2_ID, "dimitrius", "{bcrypt}$2a$10$t0lnFAcnNuJngWH/jJr1CepRENoUkCzROBrPYp/ZvHc13MdlXV38.");
    public static final Client CLIENT3 = new Client(CLIENT3_ID, "disabledclient", "{bcrypt}$2a$10$t0lnFAcnNuJngWH/jJr1CepRENoUkCzROBrPYp/ZvHc13MdlXV38.");
    public static final Client CLIENT_NEW = new Client(CLIENT_NEW_ID, "string", "{bcrypt}12121212");
    public static final ClientTo CLIENT1_TO = new ClientTo(CLIENT1.getLogin(), "password");
    public static final ClientTo CLIENT2_TO = new ClientTo(CLIENT2.getLogin(), "newpass");
    public static final ClientTo CLIENT3_TO = new ClientTo(CLIENT3.getLogin(), "newpass");
    public static final ClientTo CLIENT_NEW_TO = new ClientTo(CLIENT_NEW.getLogin(), "string");

    public static final String CLIENT1_EXPIRED_ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjkyOTIwNDkyLCJleHAiOjE2OTI5MjA0OTN9.5BLTsSAWzhMbjKr3sws3EhKBEKPFyBKCM8Eh8HNL3ul69BCph_0U88TwKnMlLXQZp7fUo3h3t29Dc9JCGNR2Eg";
    public static final String CLIENT1_NOT_VALID_ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNjkyOTIwNDkyLCJleHAiOjE2OTI5MjA0OTN9.5BLTsSAWzhMbjKr3sws3EhKBEKPFyBKCM8Eh8HNL3ul69BCph_0U88TwKnMlLXQZp7fUo3h3t29Dc9JCGNR2Eg";
    public static final String CLIENT1_EXPIRED_REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2YWxlbnRpbmUiLCJpYXQiOjE2OTI5MjA0OTIsImV4cCI6MTY5MjkyMDQ5M30.SnFr61WiGyLdmoMCJGaIENeXRIS1SVYiQAcy4UU1zKBJTKSHDPgtox4ts5XnamEUjk3lMo4jiP1YlxLAXte6yA";
    public static final String CLIENT1_NOT_VALID_REFRESH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjkyOTIwNDkyLCJleHAiOjE2OTI5MjA0OTN9.SnFr61WiGyLdmoMCJGaIENeXRIS1SVYiQAcy4UU1zKBJTKSHDPgtox4ts5XnamEUjk3lMo4jiP1YlxLAXte6yA";

    static {
        CLIENT3.setEnabled(false);
    }
}
