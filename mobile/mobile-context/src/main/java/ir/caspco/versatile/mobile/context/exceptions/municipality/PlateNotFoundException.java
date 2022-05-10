package ir.caspco.versatile.mobile.context.exceptions.municipality;

import ir.caspco.versatile.mobile.context.exceptions.MobileException;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class PlateNotFoundException extends MobileException {

    public PlateNotFoundException() {
    }

    public PlateNotFoundException(int resultNumber) {
        super(resultNumber);
    }

    public PlateNotFoundException(int resultNumber, Object... args) {
        super(resultNumber, args);
    }

    public PlateNotFoundException(Object... args) {
        super(args);
    }

    public PlateNotFoundException(String message) {
        super(message);
    }

    public PlateNotFoundException(String message, Object... args) {
        super(message, args);
    }

    public PlateNotFoundException(String message, Throwable cause, Object[] args) {
        super(message, cause, args);
    }

    public PlateNotFoundException(Throwable cause, Object... args) {
        super(cause, args);
    }

    public PlateNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object[] args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
