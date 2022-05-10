package ir.caspco.versatile.mobile.context.domains.municipality;

import ir.caspco.versatile.context.domains.AuditEntity;
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
@Table(
        name = PlateEntity.TABLE,
        schema = MOBILE,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"CUSTOMER_NUMBER", "PLATE"},
                        name = "UK_MOBILE_NO_PLATE"
                )
        }
)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlateEntity extends AuditEntity {

    public static final String TABLE = "PLATE";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = MOBILE + ".SQ_" + PlateEntity.TABLE + "_ID")
    private BigDecimal id;

    @Column(name = "CUSTOMER_NUMBER", nullable = false)
    private String customerNumber;

    @Column(name = "PLATE", nullable = false, unique = true)
    private Integer plate;

    private String description;

}
