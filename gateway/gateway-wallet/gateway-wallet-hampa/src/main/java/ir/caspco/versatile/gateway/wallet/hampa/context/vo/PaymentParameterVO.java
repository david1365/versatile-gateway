package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@Builder
public class PaymentParameterVO {
    private Object foreignTitle;
    private Object id;
    private String title;
    private String value;
}
