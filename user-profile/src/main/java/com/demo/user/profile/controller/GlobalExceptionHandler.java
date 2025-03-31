package com.demo.user.profile.controller;

import com.demo.user.profile.dto.response.APIResponse;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.utils.ResponseEntityUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final static String GENERAL_ERROR_CODE = "E0001";
    private final static String GENERAL_ERROR_MSG = "System error.";

    private void logError(String msg, Exception ex, WebRequest webRequest) {
        // if any custom log need?
        log.error(msg, ex);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<APIResponse<?>> handleGeneralException(Exception ex, WebRequest request) {
        this.logError("The application got an error: ", ex, request);
        String code = GENERAL_ERROR_CODE;
        String message = GENERAL_ERROR_MSG;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Throwable cause = ex.getCause() == null ? ex : ex.getCause();
        if (cause instanceof HttpMediaTypeNotSupportedException
                | cause instanceof HttpRequestMethodNotSupportedException) {
            status = HttpStatus.METHOD_NOT_ALLOWED;
            code = "HTTP405";
            message = cause.getMessage();
        } else if (cause instanceof ServletRequestBindingException) {
            code = "HTTP400";
            message = cause.getMessage();
            status = HttpStatus.BAD_REQUEST;
        } else if (cause instanceof HttpMessageNotReadableException) {
            message = "Invalid request parameters";
            status = HttpStatus.BAD_REQUEST;
        }

        APIResponse<?> apiResponse = APIResponse.builder()
                .code(code)
                .message(message)
                .build();
        return ResponseEntityUtils.serverError(apiResponse, status);
    }

    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<APIResponse<?>> handleApplicationException(ApplicationException ex, WebRequest request) {
        this.logError("The application got an error: ", ex, request);
        ApplicationException exception = ex.getCause() == null ? ex : (ApplicationException) ex.getCause();
        APIResponse<Object> apiResponse = createDefaltApiResponse(ex, request, exception.getCode());
        apiResponse.setData(exception.getData());
        return ResponseEntityUtils.clientError(apiResponse);
    }

    private APIResponse<Object> createDefaltApiResponse(Exception ex, WebRequest request, String errCode) {
        // TODO impl for translate
        String message = ex.getMessage();
        return APIResponse.builder()
                .code(errCode)
                .message(message)
                .build();

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<APIResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        String invalidFieldMessage = "";
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            invalidFieldMessage = error.getField() + ": " + error.getDefaultMessage();
        }
        this.logError("Invalid arguments: ", ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.builder()
                        .code("EF0001")
                        .message(String.format("Invalid fields [%s]", invalidFieldMessage))
                        .build());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(
                    violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.builder()
                        .code("EF0001")
                        .message(String.format("Invalid fields [%s]", errors))
                        .build());
    }
}
