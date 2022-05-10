package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import lombok.Data;
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
public class CardAuthorizeParamsVO {

    @NotNull
    @NotEmpty
    @NotBlank
    private String cardNumber;

    @NotNull
    @NotEmpty
    @NotBlank
    private String expirationDate;

    @NotNull
    @NotEmpty
    @NotBlank
    private String cvv2;

    @NotNull
    @NotEmpty
    @NotBlank
    private String secondPassword;

    @NotNull
    @NotEmpty
    @NotBlank
    private String pinType;

}
