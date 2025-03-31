package com.demo.payment.dto.response;

import lombok.*;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
@Value
@ToString
public class FeignResponse {

	private String code;

	private String message;

	private int statusCode;

	private Object data;

	private Map<String, Collection<String>> headers;


}
