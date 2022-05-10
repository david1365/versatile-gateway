package ir.caspco.versatile.gateway.wallet.context.repositories;


import ir.caspco.versatile.gateway.wallet.context.domains.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, BigInteger> {
    CustomerEntity findByNationalCode(String n);
}
