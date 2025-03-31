package com.demo.user.profile.service;

import com.demo.user.profile.dto.request.FundTransferRequestDTO;

public interface FundTransferService {
    void transferFunds(FundTransferRequestDTO request);
}
