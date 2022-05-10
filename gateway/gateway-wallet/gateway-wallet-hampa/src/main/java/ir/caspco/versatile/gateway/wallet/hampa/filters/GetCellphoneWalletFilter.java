package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ValueWalletInfoVO;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

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
@Path("/getCellphoneWallet")
@SuppressWarnings("unused")
public class GetCellphoneWalletFilter extends SmartGatewayFilter {

    @Request
    public ServerWebExchange mutatePath(ServerWebExchange exchange, @NotNull @Valid ValueWalletInfoVO valueWalletInfo) {

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(
                        String.format("%s/%s/wallet",
                                getProperty("wallet-hampa.paths.wallets"),
                                valueWalletInfo.getCellphone()
                        )
                )
                .method(HttpMethod.GET)
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

}

