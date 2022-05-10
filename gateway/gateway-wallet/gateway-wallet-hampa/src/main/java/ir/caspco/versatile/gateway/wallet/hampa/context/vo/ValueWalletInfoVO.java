package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import ir.caspco.versatile.common.validation.annotations.IsValidMobileNumber;
import ir.caspco.versatile.common.validation.annotations.IsValidNationalCode;
import ir.caspco.versatile.common.validation.annotations.OneIsFull;
import ir.caspco.versatile.common.validation.annotations.OneIsNotNull;
import ir.caspco.versatile.gateway.wallet.hampa.validation.annotations.IsOnTheWhiteList;
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
@OneIsNotNull(message = "ir.caspco.versatile.gateway.wallet.hampa.filters.RegisterWalletFilter.injectForRegisterWallet.valueWalletInfo.message")
public class ValueWalletInfoVO {

    private String firstName;
    private String lastName;

    @NotEmpty
    @NotBlank
    @NotNull
    @IsValidMobileNumber
    private String cellphone;

    private String fatherName;
    private String shenasnameNo;

    @OneIsFull
    @IsValidNationalCode
    private String nationalCode;

    private Long birthDate;
    private String postalCode;
    private String address;


    @IsOnTheWhiteList
    @OneIsFull
    private String customerNumber;

}
