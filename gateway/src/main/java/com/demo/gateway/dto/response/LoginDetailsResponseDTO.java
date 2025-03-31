package com.demo.gateway.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class LoginDetailsResponseDTO extends AuthResponseDTO{
    private Object profile;
    @JsonProperty("last_login_date")
    private Date lastLoginDate;
}
