package ir.caspco.versatile.gateway.city.services.municipality.core.filters;

import ir.caspco.versatile.gateway.city.services.municipality.context.properties.MunicipalityProperties;
import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.
@Filter
@Path("/debtInquiry/plate")
@SuppressWarnings("unused")
public class PlateFilter extends SmartGatewayFilter {

    private final MunicipalityProperties municipalityProperties;


    public PlateFilter(MunicipalityProperties municipalityProperties) {
        this.municipalityProperties = municipalityProperties;
    }


    @Request
    public ServerWebExchange mutatePath(ServerWebExchange exchange) {

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(getProperty("city-services-municipality.paths.plate"))
                .headers(httpHeaders -> httpHeaders.put(HttpHeaders.HOST, Collections.singletonList(municipalityProperties.getHost())))
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

}

