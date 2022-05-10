package ir.caspco.versatile.mobile.context.configuration;

import ir.caspco.versatile.context.handler.exceptions.annotations.MessagesPath;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration
@MessagesPath({
        "classpath:/i18n/municipality/mobile-municipality-field-validations",
        "classpath:/i18n/municipality/mobile-municipality-messages"
})
public class MobileContextConfiguration implements InitializingBean {

    @Value("${mobile.log.prefix}")
    private String prefix;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("mobile.full.prefix", prefix + "/mobile");
    }

}
