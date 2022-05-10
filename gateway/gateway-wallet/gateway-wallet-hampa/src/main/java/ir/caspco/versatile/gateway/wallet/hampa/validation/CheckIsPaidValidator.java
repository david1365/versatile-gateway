package ir.caspco.versatile.gateway.wallet.hampa.validation;

import ir.caspco.versatile.common.util.reflect.ReflectUtil;
import ir.caspco.versatile.common.util.ApplicationContextUtil;
import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.BillId;
import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.IsPaid;
import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.PayId;
import ir.caspco.versatile.jms.client.common.client.ValidationBillClient;
import ir.caspco.versatile.jms.client.common.msg.ValidationBillRequest;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public class CheckIsPaidValidator implements ConstraintValidator<IsPaid, Object> {

    @Override
    public boolean isValid(Object target, ConstraintValidatorContext constraintValidatorContext) {

        try {

            Optional<String> billId = ReflectUtil.fieldValue(target, BillId.class);
            Optional<String> payId = ReflectUtil.fieldValue(target, PayId.class);

            if (!billId.isPresent() || !payId.isPresent()) {
                return false;
            }

            ValidationBillClient validationBillClient = ApplicationContextUtil.getBean(ValidationBillClient.class);

            validationBillClient.validate(
                    ValidationBillRequest.builder()
                            .billNumber(billId.get())
                            .paymentNumber(payId.get())
                            .build()
            );

            return true;

        } catch (Exception e) {

            return false;

        }
    }

}
