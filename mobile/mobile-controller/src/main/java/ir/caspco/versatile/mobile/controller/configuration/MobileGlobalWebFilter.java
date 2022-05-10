package ir.caspco.versatile.mobile.controller.configuration;

import ir.caspco.versatile.common.util.BufferUtil;
import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.handler.exceptions.message.DefaultMessage;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Component
@Order(1)
public class MobileGlobalWebFilter implements WebFilter {

    @Value("${mobile.full.prefix}")
    private String prefix;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

        ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();

        String path = serverWebExchange.getRequest().getPath().pathWithinApplication().value();
        if (
                path.startsWith(prefix) &&
                        !path.endsWith("havingMobileDetails")
        ) {
            serverHttpResponse = getServerHttpResponse(serverWebExchange.getResponse());
        }

        return webFilterChain.filter(serverWebExchange.mutate().response(serverHttpResponse).build());

    }

    private ServerHttpResponse getServerHttpResponse(ServerHttpResponse serverHttpResponse) {

        return new ServerHttpResponseDecorator(serverHttpResponse) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                Flux<DataBuffer> modifiedBody = Flux.from(body).buffer()
                        .map(dataBuffers -> {

                            DataBuffer responseBody = BufferUtil.join(dataBuffers);

                            Object result = Shift.just(responseBody).toShift(Object.class).toObject();

                            DefaultMessage defaultMessage = DefaultMessage.builder()
                                    .error(false)
                                    .resultNumber(serverHttpResponse.getStatusCode().value())
                                    .data(result)
                                    .build();

                            return Shift.just(defaultMessage).toDataBuffer();

                        });


                HttpHeaders httpHeaders = getDelegate().getHeaders();

                httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);

                return getDelegate().writeWith(modifiedBody);

            }

        };

    }

}
