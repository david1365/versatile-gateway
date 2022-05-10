package ir.caspco.versatile.mobile.context.repositories.municipality;

import ir.caspco.versatile.mobile.context.domains.municipality.PlateEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface PlateRepository extends CrudRepository<PlateEntity, BigDecimal> {

    List<PlateEntity> findByCustomerNumber(String customerNumber);

}
