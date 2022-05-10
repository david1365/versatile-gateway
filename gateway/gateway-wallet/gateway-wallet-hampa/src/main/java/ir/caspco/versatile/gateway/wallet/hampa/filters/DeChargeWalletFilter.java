package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.AuxiliaryBody;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import ir.caspco.versatile.gateway.smart.filters.annotations.RequestBody;
import ir.caspco.versatile.gateway.smart.filters.annotations.ResponseBody;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.FinancialVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.WalletVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.

@SuppressWarnings("unused")
public abstract class DeChargeWalletFilter extends SmartGatewayFilter {

    protected abstract ResultVO onSuccess(ServerWebExchange exchange, ResultVO result);

    @Request
    public ServerWebExchange mutatePath(ServerWebExchange exchange, WalletVO wallet) {

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path(

                        String.format("%s/%s/withdraws",
                                getProperty("wallet-hampa.paths.wallets"),
                                wallet.getWalletId()

                        )
                ).query("trackId={uuid}").buildAndExpand(UUID.randomUUID());


        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(uriComponents.toUriString())
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

    @RequestBody
    public FinancialVO injectForRegisterWallet(FinancialVO financial) {

        return FinancialVO.builder()
                .amount(financial.getAmount())
                .desc(financial.getDesc())
                .build();

    }

    @ResponseBody
    public ResultVO injectResponseBody(ServerWebExchange exchange, ResultVO result, @AuxiliaryBody FinancialVO valueWallet) {

        if (HttpStatus.OK.equals(exchange.getResponse().getStatusCode())) {

            if (!result.isError()) {

//                ChargeWalletResultVO chargeWalletResult = Shift.just(result.getData())
//                        .toShift(ChargeWalletResultVO.class).toObject();

//                if (ChargeWalletStatusEnum.DONE.name().equals(chargeWalletResult.getStatus())) {

                return onSuccess(exchange, result);

//                }

            }

        }

        return result;

    }
}

