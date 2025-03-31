package com.demo.payment.controller;

import com.demo.payment.dto.response.APIResponse;
import io.micrometer.tracing.Tracer;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@AllArgsConstructor
public class APIResponseAdvice implements ResponseBodyAdvice<Object> {

	private final Tracer tracer;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
								  ServerHttpResponse response) {
		if (body instanceof APIResponse<?>) {
			// Get current trace context
			io.micrometer.tracing.TraceContext traceContext = tracer.currentTraceContext().context();
			if (traceContext != null) {
				String traceId = traceContext.traceId();
				((APIResponse<?>) body).setTraceId(traceId);
			}
		}
		return body;
	}
}