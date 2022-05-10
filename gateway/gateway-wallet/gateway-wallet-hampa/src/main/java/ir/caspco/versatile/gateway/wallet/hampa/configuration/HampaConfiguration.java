package ir.caspco.versatile.gateway.wallet.hampa.configuration;

import ir.caspco.versatile.context.handler.exceptions.annotations.MessagesPath;
import ir.caspco.versatile.gateway.common.context.management.configuration.RouterManagement;
import ir.caspco.versatile.gateway.common.context.properties.GatewayRoute;
import ir.caspco.versatile.gateway.smart.filters.annotations.EnableSmartFilters;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.filters.HampaBearerFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Collection;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration("wallet-hampa")
@PropertySource(
        "classpath:configuration/wallet/hampa/config.properties"
)
@EnableConfigurationProperties(HampaProperties.class)
@EnableSmartFilters(
        basePackages = "ir.caspco.versatile.gateway.wallet.hampa.filters",
        prefix = "wallet-hampa.route.filter.prefix"
)
@MessagesPath({"classpath:/i18n/hampa-field-validations", "classpath:/i18n/hampa-messages"})
public class HampaConfiguration {

    private final HampaBearerFilter hampaBearerFilter;

    private final GatewayRoute gatewayRoute;


    public HampaConfiguration(HampaBearerFilter hampaBearerFilter, HampaProperties hampaProperties) {

        this.hampaBearerFilter = hampaBearerFilter;
        this.gatewayRoute = hampaProperties.getRoute();

    }

    @RouterManagement
    public void routeLocator(RouteLocatorBuilder.Builder builder, Collection<GatewayFilter> smartFilters) {

        builder.route(gatewayRoute.getId(),
                route -> route.path(gatewayRoute.getPath())
                        .filters(
                                f -> f.filters(hampaBearerFilter::addBearerAuthHeader)
                                        .filters(smartFilters)
                                        .stripPrefix(gatewayRoute.getFilter().getStripPrefix())
                        )
                        .uri(gatewayRoute.getUri())
        );

    }

}
