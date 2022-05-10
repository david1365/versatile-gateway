package ir.caspco.versatile.gateway.wallet.pec.context.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerVO extends BaseVO {
    @JsonProperty("NationalCode")
    protected String nationalCode;

    @JsonProperty("MobileNumber")
    protected String mobileNumber;

    @JsonProperty("FirstName")
    protected String firstName;

    @JsonProperty("LastName")
    protected String lastName;

    @JsonProperty("IsActive")
    protected String isActive;

    @JsonProperty("AdditionalData")
    protected String additionalData;

    @JsonProperty("IdNumber")
    protected String idNumber;

    @JsonProperty("Location")
    protected String location;

    @JsonProperty("BirthDate")
    protected String birthDate;
}
