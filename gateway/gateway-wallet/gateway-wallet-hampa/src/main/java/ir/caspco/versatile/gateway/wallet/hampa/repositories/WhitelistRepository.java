package ir.caspco.versatile.gateway.wallet.hampa.repositories;

import ir.caspco.versatile.gateway.wallet.hampa.context.domains.WhitelistEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface WhitelistRepository extends CrudRepository<WhitelistEntity, BigDecimal> {

    Optional<WhitelistEntity> findByCustomerNumberAndActive(String CustomerNumber, Boolean active);

}
