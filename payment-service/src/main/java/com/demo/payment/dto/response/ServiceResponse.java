package com.demo.payment.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
	String id;
	String serviceId;
	String name;
}
