package ir.caspco.versatile.gateway.wallet.hampa.context.exceptions;

import ir.caspco.versatile.context.handler.exceptions.GatewayException;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class HampaException extends GatewayException {
    public HampaException() {
    }

    public HampaException(int resultNumber) {
        super(resultNumber);
    }

    public HampaException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public HampaException(Object... args) {
        super(args);
    }

    public HampaException(String message) {
        super(message);
    }

    public HampaException(String message, Object... args) {
        super(message, args);
    }

    public HampaException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public HampaException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public HampaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
