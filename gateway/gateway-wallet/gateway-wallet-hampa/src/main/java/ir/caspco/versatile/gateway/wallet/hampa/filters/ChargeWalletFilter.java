package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.gateway.smart.filters.SmartGatewayFilter;
import ir.caspco.versatile.gateway.smart.filters.annotations.*;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.domains.ChargeWalletLogEntity;
import ir.caspco.versatile.gateway.wallet.hampa.context.enums.ChargeWalletStatusEnum;
import ir.caspco.versatile.gateway.wallet.hampa.context.exceptions.ChargeWalletResponseException;
import ir.caspco.versatile.gateway.wallet.hampa.context.exceptions.PaymentIsNulException;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.*;
import ir.caspco.versatile.gateway.wallet.hampa.repositories.ChargeWalletLogRepository;
import ir.caspco.versatile.rest.client.common.esb.payment.PaymentClient;
import ir.caspco.versatile.rest.client.common.esb.payment.vo.CardAuthorizeParamsVO;
import ir.caspco.versatile.rest.client.common.esb.payment.vo.PaymentEntranceVO;
import ir.caspco.versatile.rest.client.common.esb.payment.vo.PaymentResultVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

import static ir.caspco.versatile.common.util.ISO8601.fromISO8601UTC;


/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

//TODO from davood akbari: Do not forget to test.
@Filter
@Path("/chargeWallet")
@SuppressWarnings("unused")
public class ChargeWalletFilter extends SmartGatewayFilter {

    private final PaymentClient paymentClient;
    private final ChargeWalletLogRepository chargeWalletLogRepository;


    public ChargeWalletFilter(PaymentClient paymentClient, ChargeWalletLogRepository chargeWalletLogRepository) {

        this.paymentClient = paymentClient;
        this.chargeWalletLogRepository = chargeWalletLogRepository;

    }


    @Request
    public ServerWebExchange mutatePath(ServerWebExchange exchange, @NotNull @Valid ChargeWalletVO chargeWallet, DCash<ChargeWalletLogEntity> cash) {

        ChargeWalletLogEntity chargeWalletLog = ChargeWalletLogEntity.builder()
                .trackingNoEntrance(UUID.randomUUID().toString())
                .build();

        cash.put(chargeWalletLog);

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path(
                        String.format("%s/%s/deposits",
                                getProperty("wallet-hampa.paths.wallets"),
                                chargeWallet.getWalletId()
                        )
                ).query("trackId={uuid}").buildAndExpand(chargeWalletLog.getTrackingNoEntrance());


        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .path(uriComponents.toUriString())
                .build();

        return exchange.mutate().request(mutatedRequest).build();

    }

    @RequestBody
    public FinancialVO injectForRegisterWallet(ChargeWalletVO chargeWallet, DCash<ChargeWalletLogEntity> cash) {

        PaymentEntranceVO paymentEntrance = convertToPaymentEntrance(chargeWallet, cash);
        ChargeWalletLogEntity chargeWalletLog = createChargeWalletLog(chargeWallet, paymentEntrance, cash);

        PaymentResultVo paymentResult = paymentClient.doPaymentWithoutLogin
                .post(paymentEntrance)
                .onError(error -> {

                    ChargeWalletLogEntity chargeWalletLogSaved = failed(chargeWalletLog, "doPaymentWithoutLogin");

                    cash.put(chargeWalletLogSaved);

                })
                .throwException()
                .retrieve()
                .result()
                .orElseThrow(PaymentIsNulException::new);


        ChargeWalletLogEntity chargeWalletLogSaved = paymentSucceeded(chargeWalletLog, paymentResult);

        cash.put(chargeWalletLogSaved);

        return FinancialVO.builder()
                .amount(chargeWallet.getAmount())
                .desc(chargeWallet.getDesc())
                .build();

    }

    @ResponseBody
    public ResultVO injectResponseBody(ServerWebExchange exchange, ResultVO result, DCash<ChargeWalletLogEntity> cash) {

        ChargeWalletLogEntity chargeWalletLog = cash.get();

        if (!HttpStatus.OK.equals(exchange.getResponse().getStatusCode())) {

            filterFailed(chargeWalletLog);

            throw new ChargeWalletResponseException(result.getResultNumber());

        }

        if (result.isError()) {

            filterFailed(chargeWalletLog);

            return result;

        }

        ChargeWalletResultVO chargeWalletResult = Shift.just(result.getData())
                .toShift(ChargeWalletResultVO.class).toObject();

        if (!ChargeWalletStatusEnum.DONE.status().equals(chargeWalletResult.getStatus())) {

            filterFailed(chargeWalletLog);

            return result;

        }


        chargeWalletResult.setTrackingNumber(chargeWalletLog.getTrackingNumber());
        chargeWalletResult.setTransactionDate(chargeWalletLog.getTransactionDate());


        result.setData(chargeWalletResult);

        succeeded(chargeWalletLog, result);

        return result;

    }

    private ChargeWalletLogEntity paymentSucceeded(ChargeWalletLogEntity chargeWalletLog, PaymentResultVo paymentResult) {
        chargeWalletLog.setTrackingNumber(paymentResult.getTrackingNumber());
        chargeWalletLog.setTransactionDate(paymentResult.getTransactionDate());
        return chargeWalletLogRepository.save(chargeWalletLog);
    }

    private void succeeded(ChargeWalletLogEntity chargeWalletLog, ResultVO result) {

        FinancialResultVO financialResult = Shift.just(result.getData())
                .toShift(FinancialResultVO.class).toObject();

        chargeWalletLog.setFlowStatus(FlowStatus.DONE);
        chargeWalletLog.setWalletStatementId(financialResult.getStatementId());
        chargeWalletLog.setWalletTrackDate(fromISO8601UTC(financialResult.getDate()));
        chargeWalletLog.setWalletTrackId(financialResult.getTrackId());
        chargeWalletLog.setWalletOrderId(financialResult.getOrderId());
        chargeWalletLogRepository.save(chargeWalletLog);

    }

    private void filterFailed(ChargeWalletLogEntity chargeWalletLog) {
        failed(chargeWalletLog, "ChargeWalletFilter");
    }

    private ChargeWalletLogEntity failed(ChargeWalletLogEntity chargeWalletLog, String culprit) {

        chargeWalletLog.setFlowStatus(FlowStatus.FAIL);
        chargeWalletLog.setCulprit(culprit);
        return chargeWalletLogRepository.save(chargeWalletLog);

    }

    private ChargeWalletLogEntity createChargeWalletLog(ChargeWalletVO chargeWallet,
                                                        PaymentEntranceVO paymentEntrance,
                                                        DCash<?> cash) {

        return ChargeWalletLogEntity.builder()
                .requestId(cash.requestId())
                .walletId(chargeWallet.getWalletId())
                .amount(chargeWallet.getAmount())
                .cardNumber(chargeWallet.getCardParams().getCardNumber())
                .flowStatus(FlowStatus.CREATION)
                .trackingNoEntrance(paymentEntrance.getTrackingNo())
                .build();
    }

    private PaymentEntranceVO convertToPaymentEntrance(ChargeWalletVO chargeWallet, DCash<ChargeWalletLogEntity> cash) {

        return PaymentEntranceVO.builder()
                .amount(chargeWallet.getAmount())
                .cardAuthorizeParams(
                        CardAuthorizeParamsVO.builder()
                                .cvv2(chargeWallet.getCardParams().getCvv2())
                                .expDate(chargeWallet.getCardParams().getExpirationDate())
                                .pin(chargeWallet.getCardParams().getSecondPassword())
                                .pinType(chargeWallet.getCardParams().getPinType())
                                .build()
                )
                .channelType(chargeWallet.getChannelType())
                .pan(chargeWallet.getCardParams().getCardNumber())
                .paymentServiceId(chargeWallet.getPaymentServiceId())
                .trackingNo(cash.get().getTrackingNoEntrance())
                .requireVerification(PaymentEntranceVO.defaultRequireVerification)
                .build();
    }
}

