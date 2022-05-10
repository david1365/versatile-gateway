package ir.caspco.versatile.gateway.city.services.municipality.context.configuration;

import ir.caspco.versatile.context.handler.exceptions.annotations.MessagesPath;
import ir.caspco.versatile.gateway.city.services.municipality.context.properties.MunicipalityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration
@PropertySource("classpath:configuration/city/services/municipality/${city-services-municipality.path.file}.properties")
@EnableConfigurationProperties(MunicipalityProperties.class)
@MessagesPath({"classpath:/i18n/municipality-context-field-validations", "classpath:/i18n/municipality-context-messages"})
public class MunicipalityConfiguration {
}
