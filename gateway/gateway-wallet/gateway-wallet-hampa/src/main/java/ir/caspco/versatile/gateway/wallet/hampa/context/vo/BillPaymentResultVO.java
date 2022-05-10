package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class BillPaymentResultVO {
    private String amount;
    private String billForeignTitle;
    private String billId;
    private String billTitle;
    private String billType;
    private String cycle;
    private Date date;
    private String fileCode;
    private String payId;
    private String referralNumber;
    private CardOwnerVO cardOwner;

//    private Integer trackingNumber;
//    private String reference;
//    private Date paymentDate;
//    private String channelType;
//    private BigDecimal billAmount;
//    private String billId;
//    private String billType;
//    private String billPayment;
//    private String billTitle;
}

