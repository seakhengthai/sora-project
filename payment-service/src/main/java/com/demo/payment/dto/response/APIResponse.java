package com.demo.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@Setter
@Builder
public class APIResponse<T> {

	private String code;
	private String message;
	private String title;
	@JsonProperty("message_kh")
	private String messageKh;
	String traceId;
	private T data;

	public APIResponse() {
		this.code = "S0001";
		this.message = "Success.";
	}
	public static APIResponse success() {
		return new APIResponse();
	}
	public  static <T> APIResponse success(T data) {
		return new APIResponse().data(data);
	}


	public APIResponse(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public APIResponse(String code, String message, String messageKh) {
		this.code = code;
		this.message = message;
		this.messageKh = messageKh;
	}

	public APIResponse code(String code) {
		this.code = code;
		this.message = this.getMessage(code);
		this.messageKh = this.getMessageKh(code);
		return this;
	}

	public APIResponse message(String message) {
		this.message = message;
		return this;
	}

	public APIResponse data(T data) {
		this.data = data;
		return this;
	}

	String getMessage(String code) {
		return "";
	}

	String getMessageKh(String code) {
		return "";
	}

}
