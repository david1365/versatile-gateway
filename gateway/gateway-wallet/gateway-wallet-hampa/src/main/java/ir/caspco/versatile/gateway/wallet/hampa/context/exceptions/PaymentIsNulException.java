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
public class PaymentIsNulException extends HampaException {
    public PaymentIsNulException() {
    }

    public PaymentIsNulException(int resultNumber) {
        super(resultNumber);
    }

    public PaymentIsNulException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public PaymentIsNulException(Object... args) {
        super(args);
    }

    public PaymentIsNulException(String message) {
        super(message);
    }

    public PaymentIsNulException(String message, Object... args) {
        super(message, args);
    }

    public PaymentIsNulException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public PaymentIsNulException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public PaymentIsNulException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
