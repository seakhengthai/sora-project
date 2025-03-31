package com.demo.user.profile.service;

import com.demo.user.profile.domain.entity.UserAuthDetail;

public interface UserAuthDetailService {
    UserAuthDetail findByCif(String cif);
    UserAuthDetail findByMobileNumber(String mobileNumber);
    UserAuthDetail save(UserAuthDetail userAuth);
}
