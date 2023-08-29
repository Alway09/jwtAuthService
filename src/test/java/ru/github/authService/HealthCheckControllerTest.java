package ru.github.authService;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.github.authService.controller.HealthCheckController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class HealthCheckControllerTest extends AbstractControllerTest {

    public HealthCheckControllerTest() {
        super(HealthCheckController.URL);
    }

    @Test
    public void health() throws Exception {
        perform(MockMvcRequestBuilders.get(HealthCheckController.URL))
                .andExpect(content().string("OK"));
    }
}
