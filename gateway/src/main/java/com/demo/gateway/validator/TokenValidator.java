package com.demo.gateway.validator;

import com.demo.gateway.exception.AppException;
import com.demo.gateway.exception.code.AppErrorCode;
import com.demo.gateway.utils.HttpHeaderUtils;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import static com.demo.gateway.constants.APIRequestHeaderConstants.DEVICE_ID;
import static com.demo.gateway.utils.TokenUtils.ROLES;

@Getter
@Component
public class TokenValidator {

    private Claims claims;
    private ServerWebExchange exchange;
    public TokenValidator create(ServerWebExchange exchange,
                                 Claims claims){
        this.claims=claims;
        this.exchange=exchange;
        return this;
    }

    public void validate(){
        this.accessTokenMustHaveRole();
        this.deviceMatching();
    }

    private void accessTokenMustHaveRole() {
        if (!claims.containsKey(ROLES)) {
            throw new AppException(AppErrorCode.AUTH_NO_PERMISSION);
        }
    }

    private void deviceMatching() {
        String deviceId = String.valueOf(claims.get(DEVICE_ID));
        String requestDevice = HttpHeaderUtils
                .deviceId(exchange.getRequest().getHeaders());
        if (!StringUtils.hasLength(requestDevice) || !requestDevice.equals(deviceId)) {
            throw new AppException(AppErrorCode.AUTH_INVALID_TOKEN_DEVICE);
        }
    }
}
