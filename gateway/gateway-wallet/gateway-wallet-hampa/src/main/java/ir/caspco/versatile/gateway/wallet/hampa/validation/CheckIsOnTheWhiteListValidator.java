package ir.caspco.versatile.gateway.wallet.hampa.validation;

import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.repositories.WhitelistRepository;
import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.IsOnTheWhiteList;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckIsOnTheWhiteListValidator implements ConstraintValidator<IsOnTheWhiteList, String> {

    @Override
    public boolean isValid(String customerNumber, ConstraintValidatorContext constraintValidatorContext) {

        if (customerNumber == null) {
            return false;
        }

        if (!StringUtils.hasText(customerNumber)) {
            return false;
        }

        HampaProperties hampaProperties = ApplicationContextUtil.getBean(HampaProperties.class);
        if (!hampaProperties.isCheckTheWhitelist()) {
            return true;
        }

        WhitelistRepository whitelistRepository = ApplicationContextUtil.getBean(WhitelistRepository.class);
        return whitelistRepository.findByCustomerNumberAndActive(customerNumber, true).isPresent();

    }

}
