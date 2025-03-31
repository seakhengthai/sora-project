package com.demo.user.profile.service.impl;

import com.demo.user.profile.domain.entity.UserAuthDetail;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.repository.UserAuthDetailRepository;
import com.demo.user.profile.service.UserAuthDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.demo.user.profile.exception.code.AppErrorCode.INVALID_USER;


@Service
@AllArgsConstructor
public class UserAuthDetailServiceImpl implements UserAuthDetailService {

    private final UserAuthDetailRepository userAuthDetailRepository;

    @Override
    public UserAuthDetail findByCif(String cif) {
        return userAuthDetailRepository.findFirstByCif(cif)
                .orElseThrow(()-> new ApplicationException(INVALID_USER));
    }

    @Override
    public UserAuthDetail findByMobileNumber(String mobileNumber) {
        return userAuthDetailRepository.findFirstByMobileNumber(mobileNumber)
                .orElseThrow(()-> new ApplicationException(INVALID_USER));
    }

    @Override
    public UserAuthDetail save(UserAuthDetail userAuth) {
        return userAuthDetailRepository.save(userAuth);
    }
}
