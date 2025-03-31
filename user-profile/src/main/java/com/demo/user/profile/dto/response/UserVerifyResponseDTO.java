package com.demo.user.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserVerifyResponseDTO{
    @JsonProperty("verified_id")
    String verifiedId;
    String cif;
    @JsonProperty("user_profile")
    UserProfileResponseDTO userProfile;
}
