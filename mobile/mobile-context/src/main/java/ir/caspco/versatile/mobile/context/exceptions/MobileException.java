package ir.caspco.versatile.mobile.context.exceptions;

import ir.caspco.versatile.context.handler.exceptions.VersatileException;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class MobileException extends VersatileException {

    public MobileException() {
    }

    public MobileException(int resultNumber) {
        super(resultNumber);
    }

    public MobileException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public MobileException(Object... args) {
        super(args);
    }

    public MobileException(String message) {
        super(message);
    }

    public MobileException(String message, Object... args) {
        super(message, args);
    }

    public MobileException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public MobileException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public MobileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
