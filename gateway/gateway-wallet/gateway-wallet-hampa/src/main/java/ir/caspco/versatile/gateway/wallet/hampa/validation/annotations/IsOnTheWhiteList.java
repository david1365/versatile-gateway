package ir.caspco.versatile.gateway.wallet.hampa.validation.annotations;

import ir.caspco.versatile.gateway.wallet.hampa.validation.CheckIsOnTheWhiteListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckIsOnTheWhiteListValidator.class)
@Documented
public @interface IsOnTheWhiteList {
    String message() default "{ir.caspco.versatile.common.validation.annotations.IsOnTheWhiteList.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        IsOnTheWhiteList[] value();
    }
}