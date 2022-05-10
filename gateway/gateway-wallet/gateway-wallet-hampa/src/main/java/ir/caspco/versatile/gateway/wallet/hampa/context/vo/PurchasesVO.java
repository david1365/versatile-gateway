package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import ir.caspco.versatile.common.validation.annotations.IsValidMobileNumber;
import ir.caspco.versatile.jms.client.common.enums.ChannelType;
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
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class PurchasesVO extends FinancialVO {

    public Boolean requireVerification;

    @NotNull
    @NotBlank
    @NotEmpty
    @IsValidMobileNumber
    private String walletMobileNo;

    @NotNull
    private ChannelType channelType;
}
