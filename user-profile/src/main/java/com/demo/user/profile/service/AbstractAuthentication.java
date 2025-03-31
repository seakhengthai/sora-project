package com.demo.user.profile.service;

import com.demo.user.profile.domain.VerifyType;
import com.demo.user.profile.domain.entity.UserAuthDetail;
import com.demo.user.profile.exception.ApplicationException;
import com.demo.user.profile.utils.CryptographyUtils;
import com.demo.user.profile.utils.OTPUtils;
import com.demo.user.profile.validator.UserAuthValidator;
import com.demo.user.profile.validator.UserValidatorPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.demo.user.profile.exception.code.AppErrorCode.INVALID_PIN;

@Component
@Slf4j
public abstract class AbstractAuthentication {

    @Value("${rsa.private-key}")
    private String rsaPrivateKey;

    @Value("${rsa.public-key}")
    private String rsaPublicKey;

    @Autowired
    private ApplicationContext applicationContext;

    public void userLoginValidator(UserValidatorPojo validatorPojo) {
        log.debug("Payment validation is start.");
        String[] validatorsName = applicationContext.getBeanNamesForType(UserAuthValidator.class);
        List<UserAuthValidator> validatorBeans = Arrays.stream(validatorsName)
                .map(beanName -> applicationContext.getBean(beanName, UserAuthValidator.class))
                .sorted(Comparator.comparingInt(UserAuthValidator::getRunningOrder))
                .collect(Collectors.toList());
        log.debug("Payment validation classes [{}] to execute", validatorBeans);

        List<Predicate<UserAuthValidator>> predicate = new ArrayList<>();
        if(VerifyType.ON_LOGIN.equals(validatorPojo.getVerifyType())){
            predicate = List.of(e -> e.isValidateOnLogin(validatorPojo));
        } else if (VerifyType.ON_VERIFY_PIN.equals(validatorPojo.getVerifyType())) {
            predicate = List.of(e -> e.isValidateOnVerifyPin(validatorPojo));
        }
        validatorBeans.stream()
                .filter(predicate.stream().reduce(x->true, Predicate::and))
                .forEach(paymentValidator -> paymentValidator.validate(validatorPojo));
        log.debug("Payment validation is completed.");
    }

    protected void validatePIN(String requestPwd, UserAuthDetail userAuth){
        String rawPassword = OTPUtils.decrypt(requestPwd, rsaPrivateKey);
        String password = CryptographyUtils.generatePwd(rawPassword, CryptographyUtils.salt(userAuth.getKeyMetric()));
        if (!password.equals(userAuth.getPassword())) {
            // TODO Check failed login attempts and block user
            throw new ApplicationException(INVALID_PIN);
        }
    }
}
