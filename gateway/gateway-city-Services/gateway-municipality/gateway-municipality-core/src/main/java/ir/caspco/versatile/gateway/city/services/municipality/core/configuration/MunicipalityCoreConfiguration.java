package ir.caspco.versatile.gateway.city.services.municipality.core.configuration;

import ir.caspco.versatile.context.handler.exceptions.annotations.MessagesPath;
import ir.caspco.versatile.gateway.city.services.municipality.context.properties.MunicipalityProperties;
import ir.caspco.versatile.gateway.city.services.municipality.core.filters.MunicipalityBearerFilter;
import ir.caspco.versatile.gateway.common.context.management.configuration.RouterManagement;
import ir.caspco.versatile.gateway.common.context.properties.GatewayRoute;
import ir.caspco.versatile.gateway.smart.filters.annotations.EnableSmartFilters;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Configuration("city-services-municipality")
@EnableSmartFilters(
        basePackages = "ir.caspco.versatile.gateway.city.services.municipality.core.filters",
        prefix = "city-services-municipality.route.filter.prefix"
)
@MessagesPath({"classpath:/i18n/municipality-core-field-validations", "classpath:/i18n/municipality-core-messages"})
public class MunicipalityCoreConfiguration {

    private final MunicipalityBearerFilter municipalityBearerFilter;

    private final GatewayRoute gatewayRoute;


    public MunicipalityCoreConfiguration(MunicipalityBearerFilter municipalityBearerFilter, MunicipalityProperties municipalityProperties) {

        this.municipalityBearerFilter = municipalityBearerFilter;
        this.gatewayRoute = municipalityProperties.getRoute();

    }

    @RouterManagement
    public void routeLocator(RouteLocatorBuilder.Builder builder, Collection<GatewayFilter> smartFilters) {

        builder.route(gatewayRoute.getId(),
                route -> route.path(gatewayRoute.getPath())
                        .filters(
                                f -> f.filters(municipalityBearerFilter::addBearerAuthHeader)
                                        .filters(smartFilters)
                                        .stripPrefix(gatewayRoute.getFilter().getStripPrefix())

                        )
                        .uri(gatewayRoute.getUri())
        );

    }

}
