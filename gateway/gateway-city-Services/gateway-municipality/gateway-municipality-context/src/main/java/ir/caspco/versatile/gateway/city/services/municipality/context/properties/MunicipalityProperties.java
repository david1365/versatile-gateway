package ir.caspco.versatile.gateway.city.services.municipality.context.properties;

import ir.caspco.versatile.gateway.common.context.properties.GatewayRoute;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@ConfigurationProperties(prefix = "city-services-municipality")
public class MunicipalityProperties {

    private GatewayRoute route;

    private MunicipalityPaths paths;

    private String host;

    private String clientId;

    private String clientSecret;

    private String basic;

    private String oauthAddress;
}
