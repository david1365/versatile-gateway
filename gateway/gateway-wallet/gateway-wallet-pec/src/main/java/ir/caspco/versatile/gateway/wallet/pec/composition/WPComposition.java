package ir.caspco.versatile.gateway.wallet.pec.composition;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.util.UriUtil;
import ir.caspco.versatile.gateway.wallet.pec.context.properties.WPPaths;
import ir.caspco.versatile.gateway.wallet.pec.context.properties.WPProperties;
import ir.caspco.versatile.gateway.wallet.pec.context.vo.ResultVO;
import ir.caspco.versatile.gateway.wallet.pec.context.vo.WalletVO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test for -> activateWallet
// and  Clean your code

@RestController
@RequestMapping("/wallet/pec/composite")
public class WPComposition {

    private final WPPaths wPPaths;

    private final UriUtil uriUtil;


    public WPComposition(WPProperties wpProperties, @Qualifier("WPUriUtil") UriUtil uriUtil) {
        this.wPPaths = wpProperties.getPaths();
        this.uriUtil = uriUtil;
    }

    @PostMapping("/ActivateWallet")
    public Mono<?> activateWallet(@RequestBody String body, @RequestHeader HttpHeaders headers) {

        return WebClient.create()
                .post()
                .uri(uriUtil.getUri(wPPaths.getAddCustomer()))
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .body(BodyInserters.fromValue(body))
                .exchangeToMono(addCustomerResponse -> {
                    if (!addCustomerResponse.statusCode().equals(HttpStatus.OK)) {
                        return addCustomerResponse.body(BodyExtractors.toMono(Map.class));
                    }

                    Mono<ResultVO> addCustomerResultMono = addCustomerResponse.body(BodyExtractors.toMono(ResultVO.class));

                    return addCustomerResultMono.flatMap(result -> {

                        if (result.getKeys().getKey().getResultId() == 0) {

                            WalletVO wallet = Shift.just(body)
                                    .toShift(WalletVO.class).toObject();

                            return WebClient.create()
                                    .post()
                                    .uri(uriUtil.getUri(wPPaths.getAddWallet()))
                                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                                    .body(BodyInserters.fromValue(wallet))
                                    .retrieve()
                                    .bodyToMono(Map.class);

                        }

                        return Mono.just(result);

                    });

                });
    }
}
