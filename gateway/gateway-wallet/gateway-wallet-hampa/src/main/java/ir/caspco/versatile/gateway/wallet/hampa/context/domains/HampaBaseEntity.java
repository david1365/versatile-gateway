package ir.caspco.versatile.gateway.wallet.hampa.context.domains;

import ir.caspco.versatile.gateway.wallet.context.domains.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.MappedSuperclass;
import java.util.Date;

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
public class HampaBaseEntity extends BaseEntity {
    private String walletId;
    private String walletStatementId;
    private String walletTrackId;
    private Date walletTrackDate;
    private String walletOrderId;
}