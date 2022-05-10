package ir.caspco.versatile.mobile.log.repositories;

import ir.caspco.versatile.mobile.log.domains.ReportLogEntity;
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
public interface ReportLogRepository extends CrudRepository<ReportLogEntity, BigDecimal> {
}
