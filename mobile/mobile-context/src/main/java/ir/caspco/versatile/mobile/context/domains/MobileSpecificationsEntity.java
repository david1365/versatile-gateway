package ir.caspco.versatile.mobile.context.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@Table(name =
        MobileSpecificationsEntity.TABLE,
        schema = MOBILE,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"MOBILE_BRAND", "MOBILE_MODEL", "ANDROID_VERSION"},
                        name = "UK_MB_MM_AV"
                )
        }
)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class MobileSpecificationsEntity extends BasicMobileSpecificationsEntity {

    public static final String TABLE = "MOBILE_SPECIFICATIONS";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = MOBILE + ".SQ_" + MobileSpecificationsEntity.TABLE + "_ID")
    private BigDecimal id;

    @Column(name = "MOBILE_BRAND", nullable = false)
    private String mobileBrand;

    @Column(name = "MOBILE_MODEL", nullable = false)
    private String mobileModel;

    @Column(name = "ANDROID_VERSION")
    private String androidVersion;
}
