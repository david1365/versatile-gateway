package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ValueWalletInfoVO;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.
@Filter
@Path("/getWalletList")
@SuppressWarnings("unused")
public class GetWalletListFilter extends SmartGatewayFilter {

    @Request
    public ServerWebExchange mutatePath(ServerWebExchange exchange, ValueWalletInfoVO valueWalletInfo) {

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(
                        String.format("%s/%s/list",
                                getProperty("wallet-hampa.paths.wallets"),
                                valueWalletInfo.getNationalCode()
                        )
                )
                .method(HttpMethod.GET)
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

}

