package ir.caspco.versatile.gateway.wallet.pec.configuration;

import ir.caspco.versatile.common.util.security.HeaderUtil;
import ir.caspco.versatile.context.handler.exceptions.annotations.MessagesPath;
import ir.caspco.versatile.gateway.common.context.management.configuration.RouterManagement;
import ir.caspco.versatile.gateway.common.context.properties.GatewayRoute;
import ir.caspco.versatile.gateway.smart.filters.annotations.EnableSmartFilters;
import ir.caspco.versatile.gateway.wallet.pec.context.properties.WPProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@PropertySource(
        "classpath:configuration/wallet/pec/${wallet-pec.path.file}.properties"
)
@Configuration("wallet-pec")
@EnableConfigurationProperties(WPProperties.class)
@EnableSmartFilters(
        basePackages = "ir.caspco.versatile.gateway.wallet.pec.filters",
        prefix = "wallet-pec.route.filter.prefix"
)
@MessagesPath({"classpath:/i18n/pec-field-validations", "classpath:/i18n/pec-messages"})
public class WPConfiguration {

    private final GatewayRoute gatewayRoute;


    public WPConfiguration(WPProperties wpProperties) {
        this.gatewayRoute = wpProperties.getRoute();
    }


    @RouterManagement
    public void routeLocator(RouteLocatorBuilder.Builder builder, Collection<GatewayFilter> smartFilters) {

        builder.route(gatewayRoute.getId(),
                route -> route.path(gatewayRoute.getPath())
                        .filters(defaultFilters(smartFilters))
                        .uri(gatewayRoute.getUri())
        );

    }


    private Function<GatewayFilterSpec, UriSpec> defaultFilters(Collection<GatewayFilter> smartFilters) {

        return f -> f.filter(this::addBasicAuthHeader)
                .filters(smartFilters)
                .stripPrefix(gatewayRoute.getFilter().getStripPrefix());

    }

    private Mono<Void> addBasicAuthHeader(ServerWebExchange exchange, GatewayFilterChain chain) {

        String authHeader = HeaderUtil.basic(gatewayRoute.getUserName(), gatewayRoute.getPassword());

        ServerHttpRequest requestMutated = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return chain.filter(exchange.mutate().request(requestMutated).build());

    }
}
