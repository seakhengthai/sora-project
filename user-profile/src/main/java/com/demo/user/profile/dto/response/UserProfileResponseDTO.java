package com.demo.user.profile.dto.response;

import com.demo.user.profile.domain.entity.UserAuthDetail;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserProfileResponseDTO {
    String cif;
    String fullName;
    String mobileNumber;
    String gender;
    Date dob;
    String profileId;
    String address;

    public static UserProfileResponseDTO buildFromUserAuth(UserAuthDetail userAuth){
        return UserProfileResponseDTO.builder()
                .cif(userAuth.getCif())
                .fullName(userAuth.getFullName())
                .mobileNumber(userAuth.getMobileNumber())
                .gender(userAuth.getGender())
                .dob(userAuth.getDob())
                .profileId(userAuth.getProfileId())
                .address(userAuth.getAddress())
                .build();
    }
}
