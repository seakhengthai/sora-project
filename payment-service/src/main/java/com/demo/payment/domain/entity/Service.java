package com.demo.payment.domain.entity;

import com.demo.payment.domain.TxnType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "services")
public class Service extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 3310384050294802416L;

	@Id
	private String id;

	@Column(name="service_id")
	private String serviceId;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "currency")
	private String currency;

	@Column(name = "is_has_fee")
	private Boolean isHasFee;

	@Column(name = "txn_type")
	@Enumerated(EnumType.STRING)
	private TxnType txnType;

	@Column(name = "service_commands", length = 100)
	private String serviceCommands;

	@Column(name = "channel", length = 100)
	private String channel;
}
