package com.demo.user.profile.service;

import com.demo.user.profile.domain.entity.UserAccount;
import com.demo.user.profile.dto.response.AccountInquiryResponseDTO;
import com.demo.user.profile.dto.response.AccountResponseDTO;
import org.apache.catalina.User;

import java.util.List;

public interface AccountService {
    List<AccountResponseDTO> getAccountByCif(String cif);
    AccountResponseDTO getAccountByAccountNoAndActive(String accountNo);
    AccountResponseDTO getAccountByCifAndAccountNoAndActive(String cif, String accountNo);
    AccountInquiryResponseDTO accountInquiry(String accountNo);
    UserAccount findByCifAndAccountNoAndActive(String cif, String accountNo);
    UserAccount findByAccountNoAndActive(String accountNo);
    UserAccount findByAccountNoAndActiveThenLocked(String accountNo);
    UserAccount save(UserAccount account);
}
