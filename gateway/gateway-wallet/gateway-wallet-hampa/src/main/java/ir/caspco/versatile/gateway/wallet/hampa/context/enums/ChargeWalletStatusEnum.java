package ir.caspco.versatile.gateway.wallet.hampa.context.enums;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public enum ChargeWalletStatusEnum {

    DONE(0);

    private final Integer status;

    ChargeWalletStatusEnum(int status) {
        this.status = status;
    }

    public Integer status() {
        return status;
    }
}
