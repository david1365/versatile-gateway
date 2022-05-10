package ir.caspco.versatile.mobile.log.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration
@EnableJpaRepositories("ir.caspco.versatile.mobile.log.repositories")
@EntityScan("ir.caspco.versatile.mobile.log.domains")
@Slf4j
public class MobileLogJpaConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        log.info("Mobile Log Module is configured....");
    }
}
