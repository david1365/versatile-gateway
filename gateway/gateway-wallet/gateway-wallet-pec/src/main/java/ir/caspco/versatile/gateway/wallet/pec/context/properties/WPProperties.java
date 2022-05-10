package ir.caspco.versatile.gateway.wallet.pec.context.properties;

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
@ConfigurationProperties(prefix = "wallet-pec")
public class WPProperties {

    private GatewayRoute route;

    private WPPaths paths;

}