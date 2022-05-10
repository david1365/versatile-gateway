package ir.caspco.versatile.gateway.city.services.municipality.core.filters;

import ir.caspco.versatile.common.util.security.JwtPayload;
import ir.caspco.versatile.context.enums.OAuth2GrantTypes;
import ir.caspco.versatile.context.enums.OAuth2Parameter;
import ir.caspco.versatile.context.enums.TokenType;
import ir.caspco.versatile.context.util.UriUtil;
import ir.caspco.versatile.context.vo.TokenVO;
import ir.caspco.versatile.gateway.city.services.municipality.context.properties.MunicipalityPaths;
import ir.caspco.versatile.gateway.city.services.municipality.context.properties.MunicipalityProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Component
public class MunicipalityBearerFilter {

    private final AtomicReference<TokenVO> tokenReference = new AtomicReference<>(null);

    private final MunicipalityPaths municipalityPaths;

    private final MunicipalityProperties municipalityProperties;

    private final UriUtil uriUtil;


    public MunicipalityBearerFilter(MunicipalityProperties municipalityProperties, @Qualifier("MunicipalityUriUtil") UriUtil uriUtil) {

        this.municipalityProperties = municipalityProperties;
        this.municipalityPaths = municipalityProperties.getPaths();
        this.uriUtil = uriUtil;

    }

    public Mono<Void> addBearerAuthHeader(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (tokenReference.get() == null) {
            return getToken(exchange, chain);
        }

        TokenVO token = tokenReference.get();

        if (JwtPayload.just(token.getAccessToken()).isExpired()) {
            return getToken(exchange, chain);
        }

        ServerHttpRequest requestMutated = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(token))
                .build();

        return chain.filter(exchange.mutate().request(requestMutated).build());

    }

    private Mono<Void> getToken(ServerWebExchange exchange, GatewayFilterChain chain) {

        return login(exchange)
                .retrieve()
                .bodyToMono(TokenVO.class)
                .flatMap(token -> {

                    tokenReference.set(token);

                    ServerHttpRequest requestMutated = exchange.getRequest().mutate()
                            .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(token))
                            .build();

                    return chain.filter(exchange.mutate().request(requestMutated).build());

                });

    }

    private String getAuthorizationHeader(TokenVO token) {
        return TokenType.Bearer.name() + " " + token.getAccessToken();
    }


    private WebClient.RequestHeadersSpec<?> login(ServerWebExchange exchange) {

        String basicHeader = TokenType.Basic.name() + " " + municipalityProperties.getBasic();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(OAuth2Parameter.CLIENT_ID, municipalityProperties.getClientId());
        formData.add(OAuth2Parameter.CLIENT_SECRET, municipalityProperties.getClientSecret());
        formData.add(OAuth2Parameter.GRANT_TYPE, OAuth2GrantTypes.CLIENT_CREDENTIALS.value());

        return WebClient.create()
                .post()
                .uri(municipalityProperties.getOauthAddress() + municipalityPaths.getToken())
                .headers(httpHeaders -> {
                    httpHeaders.put(HttpHeaders.AUTHORIZATION, Collections.singletonList(basicHeader));
                    httpHeaders.put(HttpHeaders.HOST, Collections.singletonList(municipalityProperties.getHost()));
                    httpHeaders.put(HttpHeaders.CONTENT_LENGTH, Collections.singletonList(Integer.toString(formData.toString().length())));
                })
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromValue(formData));

    }

}
