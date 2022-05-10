package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.BillId;
import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.IsPaid;
import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.PayId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@IsPaid
public class BillPaymentEntranceVO extends PurchasesVO {

    @NotNull
    @NotBlank
    @NotEmpty
    @BillId
    private String billId;

    @NotNull
    @NotBlank
    @NotEmpty
    @PayId
    private String payId;

}
