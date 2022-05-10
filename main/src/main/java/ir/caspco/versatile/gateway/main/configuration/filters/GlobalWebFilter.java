package ir.caspco.versatile.gateway.main.configuration.filters;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//@Component
public class GlobalWebFilter/* implements WebFilter */ {

//    @Override
//    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
//
//        ServerHttpResponse serverHttpResponse = getServerHttpResponse(serverWebExchange.getResponse());
//
//        return webFilterChain.filter(serverWebExchange.mutate().response(serverHttpResponse).build());
//
//    }
//
//    private ServerHttpResponse getServerHttpResponse(ServerHttpResponse serverHttpResponse) {
//
//        return new ServerHttpResponseDecorator(serverHttpResponse) {
//
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//
//                Flux<DataBuffer> modifiedBody = Flux.from(body).buffer()
//                        .map(dataBuffers -> {
//
//                            DataBuffer responseBody = BufferUtil.join(dataBuffers);
//                            Object result = Shift.just(responseBody).toShift(Object.class).toObject();
//
////                            if(result instanceof Map){
////                                Map<String, Object> resultMap = result;
////                                if(resultMap.get(""))
////                            }
//
//                            return responseBody;
//
//                        });
//
//
//                HttpHeaders httpHeaders = getDelegate().getHeaders();
//
//                httpHeaders.remove(HttpHeaders.CONTENT_LENGTH);
//
//                return getDelegate().writeWith(modifiedBody);
//
//            }
//
//        };
//
//    }

}
