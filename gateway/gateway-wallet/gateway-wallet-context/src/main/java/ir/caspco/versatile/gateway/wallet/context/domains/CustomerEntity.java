package ir.caspco.versatile.gateway.wallet.context.domains;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;

import static ir.caspco.versatile.context.domains.Schema.MOBILE;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@Audited
@SuperBuilder
@Entity
@Table(name = CustomerEntity.TABLE, schema = MOBILE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends BaseWalletCustomerEntity {

    public static final String TABLE = "CUSTOMER";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = MOBILE + ".SQ_" + TABLE + "_ID")
    private BigDecimal id;

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

