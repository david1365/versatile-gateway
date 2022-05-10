package ir.caspco.versatile.gateway.wallet.hampa.validation.classes;

import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.IsValidWalletId;
import org.springframework.stereotype.Component;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Component
public class WalletIdClass {

    public void walletId(@IsValidWalletId String walletId) {
    }

}
