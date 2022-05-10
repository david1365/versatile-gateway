package ir.caspco.versatile.gateway.main.configuration.filters;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.domains.GlobalLogEntity;
import ir.caspco.versatile.context.repositories.GlobalLogRepository;
import ir.caspco.versatile.gateway.smart.filters.cash.CashBuilder;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Slf4j
@Component
public class GlobalLogFilter implements GlobalFilter, Ordered {

    private final CashBuilder<GlobalLogEntity> cashBuilder;
    private final GlobalLogRepository globalLogRepository;


    public GlobalLogFilter(CashBuilder<GlobalLogEntity> cashBuilder, GlobalLogRepository globalLogRepository) {
        this.cashBuilder = cashBuilder;
        this.globalLogRepository = globalLogRepository;
    }


    private static final Set<String> LOGGABLE_CONTENT_TYPES = new HashSet<>(Arrays.asList(
            MediaType.APPLICATION_JSON_VALUE.toLowerCase(),
            MediaType.APPLICATION_JSON_UTF8_VALUE.toLowerCase(),
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.TEXT_XML_VALUE
    ));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        DCash<GlobalLogEntity> cash = cashBuilder.build(exchange);

        ServerHttpRequest requestMutated = new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {

                Logger requestLogger = new Logger(getDelegate(), cash, globalLogRepository);

                if (LOGGABLE_CONTENT_TYPES.contains(String.valueOf(getHeaders().getContentType()).toLowerCase())) {

                    return super.getBody().map(ds -> {

                        requestLogger.body(ds.asByteBuffer());
                        return ds;

                    }).doFinally((s) -> requestLogger.log());

                } else {

                    requestLogger.log();
                    return super.getBody();

                }
            }

        };


        ServerHttpResponse responseMutated = new ServerHttpResponseDecorator(exchange.getResponse()) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

                Logger responseLogger = new Logger(getDelegate(), cash, globalLogRepository);

                if (LOGGABLE_CONTENT_TYPES.contains(String.valueOf(getHeaders().getContentType()).toLowerCase())) {

                    return join(body).flatMap(db -> {

                        responseLogger.body(db.asByteBuffer());
                        responseLogger.log();

                        return getDelegate().writeWith(Mono.just(db));

                    });
                } else {

                    responseLogger.log();
                    return getDelegate().writeWith(body);

                }
            }

        };

        return chain.filter(exchange.mutate().request(requestMutated).response(responseMutated).build());

    }

    private Mono<? extends DataBuffer> join(Publisher<? extends DataBuffer> dataBuffers) {

        Assert.notNull(dataBuffers, "'dataBuffers' must not be null");
        return Flux.from(dataBuffers)
                .collectList()
                .filter((list) -> !list.isEmpty())
                .map((list) -> list.get(0).factory().join(list))
                .doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private static class Logger {

        private final StringBuilder stringBuilder = new StringBuilder();
        private final GlobalLogRepository globalLogRepository;
        private final GlobalLogEntity globalLogEntity;
        private final boolean isRequest;


        Logger(ServerHttpResponse response, DCash<GlobalLogEntity> cash, GlobalLogRepository globalLogRepository) {

            this.globalLogRepository = globalLogRepository;
            this.globalLogEntity = cash.get();
            this.isRequest = false;

            final String headers = getHeaders(response.getHeaders());
            final HttpStatus statusCode = response.getStatusCode();


            globalLogEntity.setResponseHeaders(headers);
            globalLogEntity.setStatus(statusCode);

            stringBuilder.append("\n");
            stringBuilder.append("---- Response -----").append("\n");
            stringBuilder.append("Headers      :").append(headers).append("\n");

            stringBuilder.append("Status code  :").append(statusCode).append("\n");

        }

        Logger(ServerHttpRequest request, DCash<GlobalLogEntity> cash, GlobalLogRepository globalLogRepository) {

            this.globalLogRepository = globalLogRepository;
            this.isRequest = true;

            final String headers = getHeaders(request.getHeaders());
            final HttpMethod method = request.getMethod();
            final InetSocketAddress clientIp = request.getRemoteAddress();


            this.globalLogEntity = GlobalLogEntity.builder()
                    .requestId(cash.requestId())
                    .requestHeaders(headers)
                    .httpMethod(method)
                    .build();

            if (clientIp != null) {
                globalLogEntity.setClientIp(clientIp.toString());
            }

            cash.put(globalLogEntity);

            stringBuilder.append("\n");
            stringBuilder.append("---- Request -----").append("\n");
            stringBuilder.append("Headers      :").append(headers).append("\n");
            stringBuilder.append("Method       :").append(method).append("\n");
            stringBuilder.append("Client       :").append(clientIp).append("\n");

        }

        void body(ByteBuffer byteBuffer) {

            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
            String jsonBody = charBuffer.toString();

            if (isRequest) {
                globalLogEntity.setRequestBody(jsonBody);
            } else {
                globalLogEntity.setResponseBody(jsonBody);
            }

            stringBuilder.append("Body         :").append(charBuffer).append("\n");

        }

        void log() {

            globalLogRepository.save(globalLogEntity);

            stringBuilder.append("-------------------").append("\n");

            log.debug(stringBuilder.toString());

        }

        private String getHeaders(HttpHeaders headers) {
            return Shift.just(headers.toSingleValueMap()).toJson();
        }

    }

}

