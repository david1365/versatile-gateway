package ir.caspco.versatile.gateway.wallet.context.domains;

import ir.caspco.versatile.context.domains.AuditEntity;
import ir.caspco.versatile.context.enums.FlowStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

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
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class BaseEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = MOBILE + ".SQ_WALLET_ID")
    private BigDecimal id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private FlowStatus flowStatus = FlowStatus.CREATION;

    @Column(unique = true)
    private String requestId;

    private String culprit;

}
