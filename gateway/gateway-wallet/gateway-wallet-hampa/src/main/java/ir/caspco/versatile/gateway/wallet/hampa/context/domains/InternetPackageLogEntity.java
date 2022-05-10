package ir.caspco.versatile.gateway.wallet.hampa.context.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name = InternetPackageLogEntity.TABLE, schema = MOBILE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class InternetPackageLogEntity extends BaseChargeEntity {

    public static final String TABLE = "INTERNET_PACKAGE_LOG";

}
