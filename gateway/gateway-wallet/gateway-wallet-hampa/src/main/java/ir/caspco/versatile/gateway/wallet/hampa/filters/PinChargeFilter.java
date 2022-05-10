package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.context.validation.DValidator;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.domains.PinChargeLogEntity;
import ir.caspco.versatile.gateway.wallet.hampa.context.model.ParameterModel;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.*;
import ir.caspco.versatile.gateway.wallet.hampa.repositories.PinChargeLogRepository;
import ir.caspco.versatile.jms.client.common.client.PurchaseClient;
import ir.caspco.versatile.jms.client.common.msg.PurchaseResponse;
import ir.caspco.versatile.jms.client.common.vo.PurchaseRequestVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyRequestVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyResponseVO;

import java.util.ArrayList;
import java.util.List;
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
@Path("/pinCharge")
@SuppressWarnings("unused")
public class PinChargeFilter extends WithdrawsFilter<PinChargeEntranceVO, PinChargeResultVO, PinChargeLogEntity> {

    private final PinChargeLogRepository pinChargeLogRepository;

    public PinChargeFilter(HampaProperties hampaProperties,
                           PurchaseClient purchaseClient,
                           PinChargeLogRepository pinChargeLogRepository,
                           DValidator dValidator) {

        super(hampaProperties, purchaseClient, dValidator);

        this.pinChargeLogRepository = pinChargeLogRepository;

    }

    @Override
    protected PurchaseRequestVO beforePurchase(ParameterModel<PinChargeEntranceVO, PinChargeLogEntity> param) {

        DCash<PinChargeLogEntity> cash = param.getCash();

        PinChargeLogEntity pinChargeLog = cash.get();

        int trackingNumber = getTrackingNumber(pinChargeLog.getId());

        PinChargeEntranceVO pinChargeEntrance = param.getInputRequest();

        cash.put(pinChargeLog);

        return PurchaseRequestVO.builder()
                .isBillPayment(false)
                .thirdPartyRequest(

                        ThirdPartyRequestVO.builder()
                                .walletId(pinChargeEntrance.getWalletId())
                                .paymentAmount(pinChargeEntrance.getAmount())
                                .trackingNumber(trackingNumber)
                                .walletMobileNo(Long.valueOf(pinChargeEntrance.getWalletMobileNo()))
                                .paymentServiceId((long) pinChargeEntrance.getPaymentServiceId())
                                .channelType(pinChargeEntrance.getChannelType().value())
                                .build()

                )
                .walletThirdPartyConfig(getDefaultThirdPartyConfig())

                .build();
    }

    @Override
    protected PinChargeResultVO afterPurchase(ParameterModel<PinChargeEntranceVO, PinChargeLogEntity> param, PurchaseResponse purchaseResponse) {
        ThirdPartyResponseVO thirdPartyResponse = purchaseResponse.getThirdPartyResponse();

        List<PaymentParameterVO> paymentParameters = null;
        if (thirdPartyResponse.getSerialNumber() != null && thirdPartyResponse.getPasswordNumber() != null) {
            paymentParameters = new ArrayList<>();
            paymentParameters.add(
                    PaymentParameterVO.builder()
                            .title("SERIAL_NUMBER")
                            .value(thirdPartyResponse.getSerialNumber())
                            .build());
            paymentParameters.add(
                    PaymentParameterVO.builder()
                            .title("PASSWORD")
                            .value(thirdPartyResponse.getPasswordNumber())
                            .build());
        }

        return PinChargeResultVO.builder()
                .paymentParameters(paymentParameters)
                .trackingNumber(thirdPartyResponse.getReference())
                .transactionDate(thirdPartyResponse.getPaymentDate())
                .build();
    }

    @Override
    protected void onSuccessPurchase(ParameterModel<PinChargeEntranceVO, PinChargeLogEntity> param, PinChargeResultVO result) {
        DCash<PinChargeLogEntity> cash = param.getCash();

        PinChargeLogEntity pinChargeLog = cash.get();

        PinChargeEntranceVO pinChargeEntrance = param.getInputRequest();


        setEntity(pinChargeLog, pinChargeEntrance);

        pinChargeLog.setFlowStatus(FlowStatus.DONE);
        pinChargeLog.setTransactionDate(result.getTransactionDate());
        pinChargeLog.setTrackingNumber(result.getTrackingNumber());

        if (!result.getPaymentParameters().isEmpty()) {
            pinChargeLog.setSerialNumber(result.getPaymentParameters().get(0).getValue());
            pinChargeLog.setPasswordNumber(result.getPaymentParameters().get(1).getValue());
        }

        pinChargeLogRepository.save(pinChargeLog);
    }

    @Override
    protected void onFailPurchase(ParameterModel<PinChargeEntranceVO, PinChargeLogEntity> param) {
        DCash<PinChargeLogEntity> cash = param.getCash();

        PinChargeLogEntity pinChargeLog = cash.get();

        PinChargeEntranceVO pinChargeEntrance = param.getInputRequest();


        setEntity(pinChargeLog, pinChargeEntrance);

        pinChargeLog.setFlowStatus(FlowStatus.FAIL);
        pinChargeLog.setCulprit(clientCulprit);

        pinChargeLogRepository.save(pinChargeLog);
    }

    @Override
    protected void doSuccessWallet(ParameterModel<PinChargeEntranceVO, PinChargeLogEntity> param, FinancialResultVO financialResult) {
        DCash<PinChargeLogEntity> cash = param.getCash();

        PinChargeLogEntity pinChargeLog = cash.get();
        pinChargeLog.setWalletStatementId(financialResult.getStatementId());
        pinChargeLog.setWalletTrackDate(fromISO8601UTC(financialResult.getDate()));
        pinChargeLog.setWalletTrackId(financialResult.getTrackId());
        pinChargeLog.setWalletOrderId(financialResult.getOrderId());

        cash.put(pinChargeLogRepository.save(pinChargeLog));
    }


    @Override
    protected void onCreate(PinChargeEntranceVO input, UUID uuid, DCash<PinChargeLogEntity> cash) {
        PinChargeLogEntity pinChargeLog = PinChargeLogEntity.builder()
                .walletId(input.getWalletId())
                .amount(input.getAmount())
                .flowStatus(FlowStatus.CREATION)
                .requestId(cash.requestId())
                .trackingNoEntrance(uuid.toString())
                .build();

        cash.put(pinChargeLogRepository.save(pinChargeLog));
    }

    @Override
    protected ResultVO onFailWallet(ParameterModel<PinChargeEntranceVO, PinChargeLogEntity> param) {
        DCash<PinChargeLogEntity> cash = param.getCash();

        PinChargeLogEntity pinChargeLog = cash.get();

        PinChargeEntranceVO pinChargeEntrance = param.getInputRequest();

        pinChargeLog.setFlowStatus(FlowStatus.FAIL);
        pinChargeLog.setCulprit(filterCulprit);

        setEntity(pinChargeLog, pinChargeEntrance);

        pinChargeLogRepository.save(pinChargeLog);

        return param.getResult();
    }

    private void setEntity(PinChargeLogEntity pinChargeLog, PinChargeEntranceVO pinChargeEntrance) {
        pinChargeLog.setPaymentServiceId(pinChargeEntrance.getPaymentServiceId());
        pinChargeLog.setWalletMobileNo(pinChargeEntrance.getWalletMobileNo());
    }
}

