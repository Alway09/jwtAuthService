package ru.github.authService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.github.authService.controller.AuthController;
import ru.github.authService.to.ClientTo;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    protected final String url;

    public AbstractControllerTest(String url) {
        this.url = url;
    }

    public ResultActions post(String uri, ResultMatcher expectedStatus, Object requestObject) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url + uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJSON(requestObject)))
                .andDo(print())
                .andExpect(expectedStatus);
    }

    public ResultActions postExternal(String url, ResultMatcher expectedStatus, Object requestObject) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJSON(requestObject)))
                .andDo(print())
                .andExpect(expectedStatus);
    }

    public ResultActions post(String uri, ResultMatcher expectedStatus, Object requestObject, String accessToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url + uri)
                        .header("Authorization", "Bearer ".concat(accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJSON(requestObject)))
                .andDo(print())
                .andExpect(expectedStatus);
    }

    public ResultActions put(String uri, ResultMatcher expectedStatus, Object requestObject, String accessToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(url + uri)
                        .header("Authorization", "Bearer ".concat(accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJSON(requestObject)))
                .andDo(print())
                .andExpect(expectedStatus);
    }

    public ResultActions patch(String uri, ResultMatcher expectedStatus, Object requestObject, String accessToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.patch(url + uri)
                        .header("Authorization", "Bearer ".concat(accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJSON(requestObject)))
                .andDo(print())
                .andExpect(expectedStatus);
    }

    public ResultActions patch(String uri, ResultMatcher expectedStatus, Object requestObject) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.patch(url + uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJSON(requestObject)))
                .andDo(print())
                .andExpect(expectedStatus);
    }

    public ResultActions perform(RequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

    public String getAccessToken(ClientTo clientTo) throws Exception {
        return getJsonParam(getContent(postExternal(AuthController.URL + "/signin", status().isOk(), clientTo)), "accessToken");
    }

    public String getContent(ResultActions requestResult) throws UnsupportedEncodingException {
        return requestResult.andReturn().getResponse().getContentAsString();
    }

    private String getJSON(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public String getJsonParam(String jsonString, String paramName) throws JsonProcessingException {
        return mapper.readTree(jsonString).get(paramName).asText();
    }
}
