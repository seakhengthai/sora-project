package com.demo.user.profile.service.impl;

import com.demo.user.profile.domain.Status;
import com.demo.user.profile.domain.entity.UserAccount;
import com.demo.user.profile.domain.entity.UserAuthDetail;
import com.demo.user.profile.dto.response.AccountInquiryResponseDTO;
import com.demo.user.profile.dto.response.AccountResponseDTO;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.exception.code.AppErrorCode;
import com.demo.user.profile.repository.UserAccountRepository;
import com.demo.user.profile.service.AccountService;
import com.demo.user.profile.service.UserAuthDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserAuthDetailService userAuthDetailService;

    @Override
    public List<AccountResponseDTO> getAccountByCif(String cif) {
        return userAccountRepository.findAllByCif(cif)
                .stream().map(AccountResponseDTO::buildFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponseDTO getAccountByAccountNoAndActive(String accountNo) {
        return AccountResponseDTO.buildFromEntity(this.findByAccountNoAndActive(accountNo));
    }

    @Override
    public AccountResponseDTO getAccountByCifAndAccountNoAndActive(String cif, String accountNo) {
        return AccountResponseDTO.buildFromEntity(this.findByCifAndAccountNoAndActive(cif, accountNo));
    }

    @Override
    public AccountInquiryResponseDTO accountInquiry(String accountNo) {
        UserAccount account = this.findByAccountNoAndActive(accountNo);
        UserAuthDetail userAuth = userAuthDetailService.findByCif(account.getCif());
        return AccountInquiryResponseDTO.builder()
                .name(userAuth.getFullName())
                .accountNo(accountNo)
                .currency(account.getCurrency())
                .build();
    }

    @Override
    public UserAccount findByCifAndAccountNoAndActive(String cif, String accountNo) {
        return userAccountRepository.findFirstByCifAndAccountNo(cif, accountNo)
                .filter(acc->Status.ACTIVE.equals(acc.getStatus()))
                .orElseThrow(()-> new ApplicationException(AppErrorCode.INVALID_ACCOUNT));
    }

    @Override
    public UserAccount findByAccountNoAndActive(String accountNo) {
        return userAccountRepository.findFirstByAccountNo(accountNo)
                .filter(acc->Status.ACTIVE.equals(acc.getStatus()))
                .orElseThrow(()-> new ApplicationException(AppErrorCode.INVALID_ACCOUNT));
    }

    @Override
    @Transactional
    public UserAccount findByAccountNoAndActiveThenLocked(String accountNo) {
        return userAccountRepository.findFirstByAccountNoAndStatus(accountNo, Status.ACTIVE)
                .orElseThrow(()-> new ApplicationException(AppErrorCode.INVALID_ACCOUNT));
    }

    @Override
    public UserAccount save(UserAccount account) {
        return userAccountRepository.save(account);
    }
}
