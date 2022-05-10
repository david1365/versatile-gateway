package ir.caspco.versatile.gateway.wallet.hampa.context.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ChargeWalletResponseException extends HampaException {
    public ChargeWalletResponseException() {
    }

    public ChargeWalletResponseException(int resultNumber) {
        super(resultNumber);
    }

    public ChargeWalletResponseException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public ChargeWalletResponseException(Object... args) {
        super(args);
    }

    public ChargeWalletResponseException(String message) {
        super(message);
    }

    public ChargeWalletResponseException(String message, Object... args) {
        super(message, args);
    }

    public ChargeWalletResponseException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public ChargeWalletResponseException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public ChargeWalletResponseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
