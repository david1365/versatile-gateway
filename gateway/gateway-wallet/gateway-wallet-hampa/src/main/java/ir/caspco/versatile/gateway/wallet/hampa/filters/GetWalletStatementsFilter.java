package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.WalletStatementsVO;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static ir.caspco.versatile.common.util.ISO8601.toISO8601UTC;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.
@Filter
@Path("/getWalletStatements")
@SuppressWarnings("unused")
public class GetWalletStatementsFilter extends SmartGatewayFilter {

    @Request
    @SneakyThrows
    public ServerWebExchange mutatePath(ServerWebExchange exchange, @NotNull @Valid WalletStatementsVO walletStatements) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path(

                        String.format("%s/%s/statements",
                                getProperty("wallet-hampa.paths.wallets"),
                                walletStatements.getWalletId()

                        )
                )
                .query("fromDate={f-date}")
                .query("toDate={t-date}")
                .query("length={ln}")
                .query("offset={ofs}")
                .buildAndExpand(
                        toISO8601UTC(walletStatements.getFromDate()),
                        toISO8601UTC(walletStatements.getToDate()),
                        walletStatements.getLength(),
                        walletStatements.getOffset()
                );

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(uriComponents.toUriString())
                .method(HttpMethod.GET)
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

}

