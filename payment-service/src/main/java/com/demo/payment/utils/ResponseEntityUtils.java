package com.demo.payment.utils;

import com.demo.payment.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtils {

	public static ResponseEntity<APIResponse<?>> clientError(APIResponse<?> apiResponse) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}

	public static ResponseEntity<APIResponse<?>> serverError(APIResponse<?> apiResponse) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
	}

	public static ResponseEntity<APIResponse<?>> serverError(APIResponse<?> apiResponse, HttpStatus status) {
		return ResponseEntity.status(status).body(apiResponse);
	}

	public static <T> ResponseEntity<APIResponse<?>> success(T data) {
		APIResponse<?> apiResponse = APIResponse.builder().data(data).build().success();
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	public static ResponseEntity<APIResponse<?>> errorWithStatus(HttpStatus httpStatus, APIResponse<?> apiResponse) {
		return ResponseEntity.status(httpStatus).body(apiResponse);
	}

}
