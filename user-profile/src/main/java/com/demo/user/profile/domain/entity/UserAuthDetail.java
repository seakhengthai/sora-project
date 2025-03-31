package com.demo.user.profile.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_auth_details")
public class UserAuthDetail extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "cif")
	private String cif;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "password")
	private String password;

	@Column(name = "is_enable_biometric")
	private Boolean isEnableBiometric;

	@Column(name = "biometric_token")
	private String biometricToken;

	@Column(name = "key_metric")
	private String keyMetric;

	@Column(name = "remark")
	private String remark;

	@Column(name = "is_reactivate", columnDefinition="NUMBER(1,0) DEFAULT 0")
	private Boolean isReactivate;

	@Column(name = "blocked_by")
	private String blockedBy;

	@Column(name = "profile_id")
	private String profileId;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "dob")
	private Date dob;

	@Column(name = "address")
	private String address;

	@Column(name = "last_login_date")
	private Date lastLoginDate;

}
