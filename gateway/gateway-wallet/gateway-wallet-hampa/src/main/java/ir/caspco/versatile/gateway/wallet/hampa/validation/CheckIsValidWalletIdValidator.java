package ir.caspco.versatile.gateway.wallet.hampa.validation;

import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.gateway.wallet.hampa.client.WalletClient;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.WalletInfoVO;
import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.IsValidWalletId;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckIsValidWalletIdValidator implements ConstraintValidator<IsValidWalletId, String> {

    @Override
    public boolean isValid(String walletId, ConstraintValidatorContext constraintValidatorContext) {

        if (walletId == null) {
            return true;
        }

        if (!StringUtils.hasText(walletId)) {
            return false;
        }

        WalletClient walletClient = ApplicationContextUtil.getBean(WalletClient.class);

        AtomicBoolean isValidWalletId = new AtomicBoolean(false);
        walletClient.getWalletInfo.post(WalletInfoVO.builder().walletId(walletId).build())
                .onSuccess(result -> result.ifPresent(resultVO -> isValidWalletId.set(!resultVO.isError())))
                .onError(error -> isValidWalletId.set(false))
                .retrieve();

        return isValidWalletId.get();

    }

}
