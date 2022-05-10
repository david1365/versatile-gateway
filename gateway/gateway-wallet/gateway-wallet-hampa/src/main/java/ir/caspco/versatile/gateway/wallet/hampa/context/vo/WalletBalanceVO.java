package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class WalletBalanceVO extends WalletVO {
    private Boolean cache = false;
}
