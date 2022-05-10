package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.WalletVO;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.
@Filter
@Path("/inquiry")
@SuppressWarnings("unused")
public class InquiryFilter extends SmartGatewayFilter {

    @Request
    public ServerWebExchange mutatePath(ServerWebExchange exchange, @NotNull @Valid WalletVO wallet) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path(

                        String.format("%s/%s/inquiry",
                                getProperty("wallet-hampa.paths.wallets"),
                                wallet.getWalletId()

                        )
                ).query("trackId={uuid}").buildAndExpand(wallet.getWalletId());


        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(uriComponents.toUriString())
                .method(HttpMethod.GET)
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

}

