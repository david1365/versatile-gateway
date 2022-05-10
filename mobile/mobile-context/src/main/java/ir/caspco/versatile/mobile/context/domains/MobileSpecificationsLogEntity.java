package ir.caspco.versatile.mobile.context.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
@Entity
@Table(name = MobileSpecificationsLogEntity.TABLE, schema = MOBILE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class MobileSpecificationsLogEntity extends BasicMobileSpecificationsEntity {

    public static final String TABLE = "MOBILE_SPECIFICATIONS_LOG";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = MOBILE + ".SQ_MOB_SPC_LG_ID")
    private BigDecimal id;

    private String mobileBrand;
    private String mobileModel;
    private String androidVersion;

    private String nationalCode;
    private String phoneNumber;

    private Boolean existsIn;
}
