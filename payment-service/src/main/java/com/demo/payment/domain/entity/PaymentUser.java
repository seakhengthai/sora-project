package com.demo.payment.domain.entity;

import com.demo.payment.domain.PaymentUserType;
import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Builder
@Entity
@Table(name = "payment_users")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentUser extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tx_order_id")
	private TxOrder txOrder;

	@Column(name = "cif")
	private String cif;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "account_ccy")
	private String accountCcy;

	@Column(name = "user_type", length = 10)
	@Enumerated(EnumType.STRING)
	private PaymentUserType userType;
}