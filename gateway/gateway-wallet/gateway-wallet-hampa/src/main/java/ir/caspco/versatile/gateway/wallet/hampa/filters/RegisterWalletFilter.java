package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import ir.caspco.versatile.gateway.smart.filters.annotations.RequestBody;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ValueWalletInfoVO;
import ir.caspco.versatile.jms.client.common.client.CustomerClient;
import ir.caspco.versatile.jms.client.common.vo.CompleteCustomerInfoVO;
import ir.caspco.versatile.jms.client.common.vo.UserRequestVO;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
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
@Path("/registerWallet")
@SuppressWarnings("unused")
@Validated
public class RegisterWalletFilter extends SmartGatewayFilter {

    private final CustomerClient customerClient;


    public RegisterWalletFilter(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }


    @Request
    public ServerWebExchange mutatePath(ServerWebExchange exchange) {

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(getProperty("wallet-hampa.paths.wallets"))
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

    @RequestBody
    public ValueWalletInfoVO injectForRegisterWallet(@NotNull @Valid ValueWalletInfoVO valueWalletInfo) {

        UserRequestVO userRequest = UserRequestVO.builder()
                .customerNo(valueWalletInfo.getCustomerNumber())
                .individualNationalCode(valueWalletInfo.getNationalCode())
                .build();

        CompleteCustomerInfoVO customerInfo = customerClient.getCustomerInfo(userRequest);


        return ValueWalletInfoVO.builder()
                .nationalCode(customerInfo.getNationalNo())
                .cellphone(valueWalletInfo.getCellphone())
                .firstName(customerInfo.getName())
                .lastName(customerInfo.getFamily())
                .fatherName(customerInfo.getFatherName())
                .shenasnameNo(customerInfo.getIdentificationNo())
                .birthDate(customerInfo.getBirthDate().getTime() / 1000)
                .postalCode(customerInfo.getPostalCode())
                .address(customerInfo.getAddress())
                .build();

    }

}

