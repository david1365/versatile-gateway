package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.validation.DValidator;
import ir.caspco.versatile.gateway.smart.filters.annotations.Request;
import ir.caspco.versatile.gateway.smart.filters.annotations.RequestBody;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.model.ParameterModel;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.FinancialResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.FinancialVO;
import ir.caspco.versatile.jms.client.common.client.PurchaseClient;
import ir.caspco.versatile.jms.client.common.exceptions.PurchaseResponseNullPointerException;
import ir.caspco.versatile.jms.client.common.msg.PurchaseResponse;
import ir.caspco.versatile.jms.client.common.vo.PurchaseRequestVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyConfigVO;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.UUID;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.
@SuppressWarnings("unused")
public abstract class WithdrawsFilter<I, O, C> extends DecisionFilter<I, O, C> {

    protected static final String clientCulprit = "PurchaseClient";

    protected final String filterCulprit = getClass().getSimpleName();

    private final PurchaseClient purchaseClient;

    private final HampaProperties hampaProperties;

    private final DValidator dValidator;


    public WithdrawsFilter(HampaProperties hampaProperties, PurchaseClient purchaseClient, DValidator dValidator) {
        this.hampaProperties = hampaProperties;

        this.purchaseClient = purchaseClient;

        this.dValidator = dValidator;
    }


    @Request
    @SuppressWarnings("unchecked")
    public ServerWebExchange mutatePath(ServerWebExchange exchange, I inputMap, DCash<C> cash) {

        UUID uuid = UUID.randomUUID();

        Object input = Shift.just(inputMap).toShift(firstGenericClassObject()).toObject();

        validate(input);

        FinancialVO financial = (FinancialVO) input;

        onCreate((I) input, uuid, cash);


        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path(

                        String.format("%s/%s/withdraws",
                                getProperty("wallet-hampa.paths.wallets"),
                                financial.getWalletId()

                        )
                ).query("trackId={uuid}").buildAndExpand(uuid);


        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(uriComponents.toUriString())
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

    @RequestBody
    public FinancialVO mutateRequestBody(ServerWebExchange exchange, FinancialVO financial) {
        return FinancialVO.builder()
//                .merchantId(hampaProperties.getMerchantId())
                .amount(financial.getAmount())
                .desc(financial.getDesc())
                .build();
    }

    protected abstract PurchaseRequestVO beforePurchase(ParameterModel<I, C> param);

    protected abstract O afterPurchase(ParameterModel<I, C> param, PurchaseResponse purchaseResponse);

    protected abstract void onSuccessPurchase(ParameterModel<I, C> param, O result);

    protected abstract void onFailPurchase(ParameterModel<I, C> param);

    protected abstract void doSuccessWallet(ParameterModel<I, C> param, FinancialResultVO financialResult);


    protected O onSuccessWallet(ParameterModel<I, C> param) {

        doSuccessWallet(param, Shift.just(param.getResult().getData()).toShift(FinancialResultVO.class).toObject());

        PurchaseRequestVO purchaseRequest = beforePurchase(param);

        PurchaseResponse purchaseResponse = purchaseClient.purchase(purchaseRequest)
                .onError(error -> onFailPurchase(param))
                .throwException()
                .retrieve()
                .result()
                .orElseThrow(PurchaseResponseNullPointerException::new);

        O output = afterPurchase(param, purchaseResponse);

        onSuccessPurchase(param, output);

        return output;

    }

    protected int getTrackingNumber(BigDecimal id) {

        final int maxSize = 6;

        String strTrackingNumber = id.toString();
        int len = strTrackingNumber.length();
        if (len > maxSize) {
            strTrackingNumber = strTrackingNumber.substring(len - maxSize);
        }

        return Integer.parseInt(strTrackingNumber);

    }

    protected ThirdPartyConfigVO getDefaultThirdPartyConfig() {
        return ThirdPartyConfigVO.builder().id(hampaProperties.getDefaultThirdPartyConfigId()).build();
    }

    private void validate(Object input) {
        dValidator.validate(input);
    }

}

