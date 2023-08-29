package ru.github.authService.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().is4xxClientError()
                || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        String errorBody = new BufferedReader(new InputStreamReader(httpResponse.getBody()))
                .lines().collect(Collectors.joining("\n"));

        log.error("Response error. code: " + httpResponse.getStatusCode() + "; body: " + errorBody);
    }
}
