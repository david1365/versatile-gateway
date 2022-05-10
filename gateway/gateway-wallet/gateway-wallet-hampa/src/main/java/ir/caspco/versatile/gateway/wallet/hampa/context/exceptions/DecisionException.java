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
public class DecisionException extends HampaException {
    public DecisionException() {
    }

    public DecisionException(int resultNumber) {
        super(resultNumber);
    }

    public DecisionException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public DecisionException(Object... args) {
        super(args);
    }

    public DecisionException(String message) {
        super(message);
    }

    public DecisionException(String message, Object... args) {
        super(message, args);
    }

    public DecisionException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public DecisionException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public DecisionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
