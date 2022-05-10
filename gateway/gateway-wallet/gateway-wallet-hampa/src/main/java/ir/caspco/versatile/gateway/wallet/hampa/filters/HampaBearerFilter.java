package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.util.UriUtil;
import ir.caspco.versatile.context.vo.AccountVO;
import ir.caspco.versatile.gateway.common.context.properties.GatewayRoute;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaPaths;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.TokenVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
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
public class HampaBearerFilter {

    private static final String prefixToken = "Bearer ";

    private final AtomicReference<TokenVO> tokenReference = new AtomicReference<>(null);

    private final HampaPaths wHPaths;

    private final GatewayRoute gatewayRoute;

    private final UriUtil uriUtil;


    public HampaBearerFilter(HampaProperties wHProperties, @Qualifier("WHUriUtil") UriUtil uriUtil) {

        this.gatewayRoute = wHProperties.getRoute();
        this.wHPaths = wHProperties.getPaths();
        this.uriUtil = uriUtil;

    }

    public Mono<Void> addBearerAuthHeader(ServerWebExchange exchange, GatewayFilterChain chain) {

        if (tokenReference.get() == null) {
            return getToken(exchange, chain);
        }

        TokenVO token = tokenReference.get();
        String accessToken = token.getAccessToken();

        if (token.isExpired()) {
            return getToken(exchange, chain);
        }

        ServerHttpRequest requestMutated = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, prefixToken + accessToken)
                .build();

        return chain.filter(exchange.mutate().request(requestMutated).build());

    }

    private Mono<Void> getToken(ServerWebExchange exchange, GatewayFilterChain chain) {
        return login()
                .retrieve()
                .bodyToMono(ResultVO.class)
                .flatMap(result -> {

                    tokenReference.set(Shift.just(result.getData()).toShift(TokenVO.class).toObject());

                    ServerHttpRequest requestMutated = exchange.getRequest().mutate()
                            .header(HttpHeaders.AUTHORIZATION, prefixToken + tokenReference.get().getAccessToken())
                            .build();

                    return chain.filter(exchange.mutate().request(requestMutated).build());

                });
    }


    private WebClient.RequestHeadersSpec<?> login() {

        AccountVO account = AccountVO.builder()
                .username(gatewayRoute.getUserName())
                .password(gatewayRoute.getPassword())
                .build();


        return WebClient.create()
                .post()
                .uri(uriUtil.getRealUri(wHPaths.getLogin()))
                .headers(httpHeaders -> {
                    httpHeaders.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
                    httpHeaders.put(HttpHeaders.ACCEPT, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
                })
                .body(BodyInserters.fromValue(account));

    }

}
