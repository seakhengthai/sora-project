package com.demo.payment.config.fiegn;

import com.demo.payment.domain.UserAccount;
import com.demo.payment.dto.request.FundTransferRequestDTO;
import com.demo.payment.dto.response.APIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "userProfileFeignClient", url = "${user-profile.base-url:http://localhost:9091/user-profile}")
public interface UserProfileFeignClient {

	@GetMapping("/api/v1.0/accounts/internal/account/{accountNo}")
	APIResponse<UserAccount> getAccountByAccountNo(@PathVariable("accountNo") String accountNo);

	@GetMapping("/api/v1.0/accounts/internal/cif/{cif}/account/{accountNo}")
	APIResponse<UserAccount> getAccountByCifAndAccountNo(@PathVariable("cif") String cif, @PathVariable("accountNo") String accountNo);

	@PostMapping("/api/v1.0/accounts/transfer")
	void transferFunds(@RequestHeader Map<String, String> headers, @RequestBody FundTransferRequestDTO request);
}
