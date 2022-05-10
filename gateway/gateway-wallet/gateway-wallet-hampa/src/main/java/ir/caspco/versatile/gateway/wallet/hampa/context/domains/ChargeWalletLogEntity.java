package ir.caspco.versatile.gateway.wallet.hampa.context.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

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
@Table(name = ChargeWalletLogEntity.TABLE, schema = MOBILE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChargeWalletLogEntity extends HampaBaseEntity {

    public static final String TABLE = "CHARGE_WALLET_LOG";

    private BigDecimal amount;
    private String cardNumber;
    private String trackingNumber;
    private Date transactionDate;
    private String trackingNoEntrance;

}
