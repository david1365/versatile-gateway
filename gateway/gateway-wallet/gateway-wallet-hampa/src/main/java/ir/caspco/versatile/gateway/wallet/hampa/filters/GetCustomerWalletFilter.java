package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ValueWalletInfoVO;
import ir.caspco.versatile.jms.client.common.client.CustomerClient;
import ir.caspco.versatile.jms.client.common.vo.CompleteCustomerInfoVO;
import ir.caspco.versatile.jms.client.common.vo.UserRequestVO;
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
@Path("/getCustomerWallet")
@SuppressWarnings("unused")
public class GetCustomerWalletFilter extends SmartGatewayFilter {

    private final CustomerClient customerClient;


    public GetCustomerWalletFilter(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }


    @Request
    public ServerWebExchange mutatePath(ServerWebExchange exchange, @NotNull @Valid ValueWalletInfoVO valueWalletInfo) {

        if (valueWalletInfo.getCustomerNumber() != null) {

            UserRequestVO userRequest = UserRequestVO.builder()
                    .customerNo(valueWalletInfo.getCustomerNumber())
                    .build();

            CompleteCustomerInfoVO customerInfo = customerClient.getCustomerInfo(userRequest);


            valueWalletInfo.setNationalCode(customerInfo.getNationalNo());

        }

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(String.format("%s/%s/%s/wallet",
                        getProperty("wallet-hampa.paths.wallets"),
                        valueWalletInfo.getNationalCode(),
                        valueWalletInfo.getCellphone()
                        )
                )
                .method(HttpMethod.GET)
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

}

