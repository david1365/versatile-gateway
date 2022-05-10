package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import ir.caspco.versatile.common.validation.annotations.IsValidMobileNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Email;
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
public class ChargeWalletVO extends FinancialVO {

    @NotNull
    @Valid
    private CardAuthorizeParamsVO cardParams;

    @IsValidMobileNumber
    private String cellphone;

    @Email
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    private String channelType;

    @NotNull
    @NotBlank
    @NotEmpty
    private String paymentServiceId;
}
