package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.context.validation.DValidator;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.domains.DirectChargeLogEntity;
import ir.caspco.versatile.gateway.wallet.hampa.context.model.ParameterModel;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.*;
import ir.caspco.versatile.gateway.wallet.hampa.repositories.DirectChargeLogRepository;
import ir.caspco.versatile.jms.client.common.client.PurchaseClient;
import ir.caspco.versatile.jms.client.common.msg.PurchaseResponse;
import ir.caspco.versatile.jms.client.common.vo.PurchaseRequestVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyRequestVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyResponseVO;

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
@Path("/directCharge")
@SuppressWarnings("unused")
public class DirectChargeFilter extends WithdrawsFilter<ChargeEntranceVO, ChargeResultVO, DirectChargeLogEntity> {

    private final DirectChargeLogRepository directChargeLogRepository;

    public static final Long defaultPaymentServiceId = 0L;

    public DirectChargeFilter(HampaProperties hampaProperties,
                              PurchaseClient purchaseClient,
                              DirectChargeLogRepository directChargeLogRepository,
                              DValidator dValidator) {

        super(hampaProperties, purchaseClient, dValidator);

        this.directChargeLogRepository = directChargeLogRepository;

    }


    @Override
    protected void onCreate(ChargeEntranceVO input, UUID uuid, DCash<DirectChargeLogEntity> cash) {

        DirectChargeLogEntity directChargeLog = DirectChargeLogEntity.builder()
                .walletId(input.getWalletId())
                .amount(input.getAmount())
                .flowStatus(FlowStatus.CREATION)
                .requestId(cash.requestId())
                .trackingNoEntrance(uuid.toString())
                .build();

        cash.put(directChargeLogRepository.save(directChargeLog));

    }

    @Override
    protected PurchaseRequestVO beforePurchase(ParameterModel<ChargeEntranceVO, DirectChargeLogEntity> param) {
        DCash<DirectChargeLogEntity> cash = param.getCash();

        DirectChargeLogEntity directChargeLog = cash.get();

        ChargeEntranceVO chargeEntrance = param.getInputRequest();

        int trackingNumber = getTrackingNumber(directChargeLog.getId());

        cash.put(directChargeLog);

        return PurchaseRequestVO.builder()
                .isBillPayment(false)
                .thirdPartyRequest(
                        ThirdPartyRequestVO.builder()
                                .walletId(chargeEntrance.getWalletId())
                                .paymentAmount(chargeEntrance.getAmount())
                                .trackingNumber(trackingNumber)
                                .walletMobileNo(Long.valueOf(chargeEntrance.getWalletMobileNo()))
                                .paymentServiceId(defaultPaymentServiceId)
                                .channelType(chargeEntrance.getChannelType().value())
                                .mobileNo(chargeEntrance.getMobileNo())
                                .chTopupServiceCode(chargeEntrance.getChTopupServiceCode().id())
                                .build()

                )
                .walletThirdPartyConfig(getDefaultThirdPartyConfig())
                .build();
    }

    @Override
    protected ChargeResultVO afterPurchase(ParameterModel<ChargeEntranceVO, DirectChargeLogEntity> param, PurchaseResponse purchaseResponse) {

        DCash<DirectChargeLogEntity> cash = param.getCash();

        DirectChargeLogEntity directChargeLog = cash.get();

        cash.put(directChargeLog);

        int trackingNumber = getTrackingNumber(directChargeLog.getId());

        ThirdPartyResponseVO thirdPartyResponse = purchaseResponse.getThirdPartyResponse();

        return ChargeResultVO.builder()
                .trackingNo(String.valueOf(trackingNumber))
                .transactionNumber(thirdPartyResponse.getReference())
                .transactionDate(thirdPartyResponse.getPaymentDate())
                .totalBalance(TotalBalanceVO.builder().build())
                .ledgerBalance(LedgerBalanceVO.builder().build())
                .build();

    }

    @Override
    protected void onSuccessPurchase(ParameterModel<ChargeEntranceVO, DirectChargeLogEntity> param, ChargeResultVO result) {

        DCash<DirectChargeLogEntity> cash = param.getCash();

        DirectChargeLogEntity directChargeLog = cash.get();

        ChargeEntranceVO chargeEntrance = param.getInputRequest();


        setEntity(directChargeLog, chargeEntrance);

        directChargeLog.setFlowStatus(FlowStatus.DONE);
        directChargeLog.setTransactionDate(result.getTransactionDate());
        directChargeLog.setTransactionNumber(result.getTransactionNumber());

        directChargeLogRepository.save(directChargeLog);

    }

    @Override
    protected void onFailPurchase(ParameterModel<ChargeEntranceVO, DirectChargeLogEntity> param) {
        DCash<DirectChargeLogEntity> cash = param.getCash();

        DirectChargeLogEntity directChargeLog = cash.get();

        ChargeEntranceVO chargeEntrance = param.getInputRequest();


        setEntity(directChargeLog, chargeEntrance);

        directChargeLog.setFlowStatus(FlowStatus.FAIL);
        directChargeLog.setCulprit(clientCulprit);

        directChargeLogRepository.save(directChargeLog);
    }

    @Override
    protected void doSuccessWallet(ParameterModel<ChargeEntranceVO, DirectChargeLogEntity> param, FinancialResultVO financialResult) {
        DCash<DirectChargeLogEntity> cash = param.getCash();

        DirectChargeLogEntity directChargeLog = cash.get();
        directChargeLog.setWalletStatementId(financialResult.getStatementId());
        directChargeLog.setWalletTrackDate(fromISO8601UTC(financialResult.getDate()));
        directChargeLog.setWalletTrackId(financialResult.getTrackId());
        directChargeLog.setWalletOrderId(financialResult.getOrderId());

        cash.put(directChargeLogRepository.save(directChargeLog));
    }

    @Override
    protected ResultVO onFailWallet(ParameterModel<ChargeEntranceVO, DirectChargeLogEntity> param) {
        DCash<DirectChargeLogEntity> cash = param.getCash();

        DirectChargeLogEntity directChargeLog = cash.get();

        ChargeEntranceVO chargeEntranceVO = param.getInputRequest();

        directChargeLog.setFlowStatus(FlowStatus.FAIL);
        directChargeLog.setCulprit(filterCulprit);

        setEntity(directChargeLog, chargeEntranceVO);

        directChargeLogRepository.save(directChargeLog);

        return param.getResult();
    }

    private void setEntity(DirectChargeLogEntity directChargeLog, ChargeEntranceVO chargeEntrance) {
        directChargeLog.setChTopupServiceCode(chargeEntrance.getChTopupServiceCode());
        directChargeLog.setMobileNo(chargeEntrance.getMobileNo());
        directChargeLog.setWalletMobileNo(chargeEntrance.getWalletMobileNo());
    }

}

