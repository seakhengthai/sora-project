package com.demo.payment.exception;

import com.demo.payment.dto.response.FeignResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class FeignClientException extends RuntimeException implements ErrorDecoder {


    private int httpStatus;
    private String code;
    private String message;
    private Object data;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String ERROR_CODE = "F0001";
    private final String ERROR_MSG = "Feign error.";
    private final int STATUS = 400;
    private static final String FEIGN_ERROR_MESSAGE = "url [%s] method [%s] body [%s] status [%s]";

    public static FeignClientException create(String code,Object data){
        FeignClientException feignClientException = new FeignClientException();
        feignClientException.code=code;
        feignClientException.data=data;
        return feignClientException;
    }

    @SneakyThrows
    @Override
    public Exception decode(String s, Response response) {
        FeignResponse.FeignResponseBuilder feignResponseBuilder = FeignResponse.builder()
                .headers(response.headers()).statusCode(response.status());
        if (response.body() == null) {
            String errorMessage = String
                    .format(FEIGN_ERROR_MESSAGE, response.request().url(),
                            response.request().httpMethod().name(), null,
                            response.status());
            log.error("Feign error details:{}", errorMessage);
            return new FeignClientException(STATUS, ERROR_CODE, ERROR_MSG, null);
        }
        int feignStatus = response.status();
        String bodyMessage = getResponseBodyAsString(response.body());
        Map<String, Object> bodyResponse = objectMapper
                .readValue(bodyMessage, new TypeReference<Map<String, Object>>() {
                });
        if (bodyResponse.get("code") != null && bodyResponse.get("code") != null) {
            feignResponseBuilder.code(String.valueOf(bodyResponse.get("code")));
            feignResponseBuilder.message(String.valueOf(bodyResponse.get("message")));
            feignResponseBuilder.data(Objects.nonNull(bodyResponse.getOrDefault("data", null)));
        }
        FeignResponse feignResponse = feignResponseBuilder.build();
        String errorMessage = String
                .format(FEIGN_ERROR_MESSAGE, response.request().url(),
                        response.request().httpMethod().name(), feignResponse,
                        feignResponse.getStatusCode());
        log.error("Feign error details:{}", errorMessage);
        return new FeignClientException(
                feignStatus, feignResponse.getCode(), feignResponse.getMessage(), feignResponse.getData());
    }

    private String getResponseBodyAsString(final Response.Body body) {
        StringBuilder responseString = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(body.asInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseString.append(line);
            }
        } catch (IOException e) {
            // Log error or throw a custom exception as needed
            log.error("Failed to read the response body with error: ", e);
        }
        return responseString.toString();
    }

}
