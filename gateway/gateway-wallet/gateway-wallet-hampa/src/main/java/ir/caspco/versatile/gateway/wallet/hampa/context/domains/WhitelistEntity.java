package ir.caspco.versatile.gateway.wallet.hampa.context.domains;

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
@Table(name = WhitelistEntity.TABLE, schema = MOBILE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class WhitelistEntity extends AuditEntity {

    public static final String TABLE = "WHITELIST";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = MOBILE + ".SQ_" + WhitelistEntity.TABLE + "_ID")
    private BigDecimal id;

    @Column(unique = true, nullable = false)
    private String customerNumber;

    @Column(unique = true, nullable = false)
    private String cellphone;

    @Column(nullable = false)
    private boolean active;

}
