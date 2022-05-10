package ir.caspco.versatile.gateway.wallet.hampa.repositories;

import ir.caspco.versatile.gateway.wallet.hampa.context.domains.PinChargeLogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface PinChargeLogRepository extends CrudRepository<PinChargeLogEntity, BigDecimal> {
}
