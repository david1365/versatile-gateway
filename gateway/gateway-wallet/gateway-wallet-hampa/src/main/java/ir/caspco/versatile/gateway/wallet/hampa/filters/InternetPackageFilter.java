package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.context.validation.DValidator;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.domains.InternetPackageLogEntity;
import ir.caspco.versatile.gateway.wallet.hampa.context.model.ParameterModel;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.*;
import ir.caspco.versatile.gateway.wallet.hampa.repositories.InternetPackageLogRepository;
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
@Path("/internetPackage")
@SuppressWarnings("unused")
public class InternetPackageFilter extends WithdrawsFilter<InternetPackageEntranceVO, ChargeResultVO, InternetPackageLogEntity> {

    public static final Long defaultPaymentServiceId = 0L;

    private final InternetPackageLogRepository internetPackageLogRepository;


    public InternetPackageFilter(HampaProperties hampaProperties,
                                 PurchaseClient purchaseClient,
                                 InternetPackageLogRepository internetPackageLogRepository,
                                 DValidator dValidator) {

        super(hampaProperties, purchaseClient, dValidator);

        this.internetPackageLogRepository = internetPackageLogRepository;

    }

    @Override
    protected PurchaseRequestVO beforePurchase(ParameterModel<InternetPackageEntranceVO, InternetPackageLogEntity> param) {

        InternetPackageEntranceVO internetPackageEntrance = param.getInputRequest();

        DCash<InternetPackageLogEntity> cash = param.getCash();

        InternetPackageLogEntity internetPackageLog = cash.get();

        int trackingNumber = getTrackingNumber(internetPackageLog.getId());

        cash.put(internetPackageLog);

        return PurchaseRequestVO.builder()
                .isBillPayment(false)
                .thirdPartyRequest(

                        ThirdPartyRequestVO.builder()
                                .walletId(internetPackageEntrance.getWalletId())
                                .paymentAmount(internetPackageEntrance.getAmount())
                                .trackingNumber(trackingNumber)
                                .walletMobileNo(Long.valueOf(internetPackageEntrance.getWalletMobileNo()))
                                .channelType(internetPackageEntrance.getChannelType().value())
                                .mobileNo(internetPackageEntrance.getMobileNo())
                                .paymentServiceId(defaultPaymentServiceId)
                                .chTopupServiceCode(internetPackageEntrance.getChTopupServiceCode().id())
                                .productId(Integer.valueOf(internetPackageEntrance.getProductId()))
                                .operatorId(Integer.valueOf(internetPackageEntrance.getOperatorId()))
                                .build()

                )
                .walletThirdPartyConfig(getDefaultThirdPartyConfig())
                .build();

    }

    @Override
    protected ChargeResultVO afterPurchase(ParameterModel<InternetPackageEntranceVO, InternetPackageLogEntity> param, PurchaseResponse purchaseResponse) {

        DCash<InternetPackageLogEntity> cash = param.getCash();

        InternetPackageLogEntity internetPackageLog = cash.get();

        int trackingNumber = getTrackingNumber(internetPackageLog.getId());

        cash.put(internetPackageLog);

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
    protected void onCreate(InternetPackageEntranceVO input, UUID uuid, DCash<InternetPackageLogEntity> cash) {

        InternetPackageLogEntity internetPackageLog = InternetPackageLogEntity.builder()
                .walletId(input.getWalletId())
                .amount(input.getAmount())
                .flowStatus(FlowStatus.CREATION)
                .requestId(cash.requestId())
                .trackingNoEntrance(uuid.toString())
                .build();

        cash.put(internetPackageLogRepository.save(internetPackageLog));

    }

    @Override
    protected void onSuccessPurchase(ParameterModel<InternetPackageEntranceVO, InternetPackageLogEntity> param, ChargeResultVO result) {

        DCash<InternetPackageLogEntity> cash = param.getCash();

        InternetPackageLogEntity internetPackageLog = cash.get();

        InternetPackageEntranceVO internetPackageEntrance = param.getInputRequest();


        setEntity(internetPackageLog, internetPackageEntrance);

        internetPackageLog.setFlowStatus(FlowStatus.DONE);
        internetPackageLog.setTransactionDate(result.getTransactionDate());
        internetPackageLog.setTransactionNumber(result.getTransactionNumber());

        internetPackageLogRepository.save(internetPackageLog);

    }

    @Override
    protected void onFailPurchase(ParameterModel<InternetPackageEntranceVO, InternetPackageLogEntity> param) {
        DCash<InternetPackageLogEntity> cash = param.getCash();

        InternetPackageLogEntity internetPackageLog = cash.get();

        InternetPackageEntranceVO internetPackageEntrance = param.getInputRequest();


        setEntity(internetPackageLog, internetPackageEntrance);

        internetPackageLog.setFlowStatus(FlowStatus.FAIL);
        internetPackageLog.setCulprit(clientCulprit);

        internetPackageLogRepository.save(internetPackageLog);
    }

    @Override
    protected void doSuccessWallet(ParameterModel<InternetPackageEntranceVO, InternetPackageLogEntity> param, FinancialResultVO financialResult) {
        DCash<InternetPackageLogEntity> cash = param.getCash();

        InternetPackageLogEntity internetPackageLog = cash.get();
        internetPackageLog.setWalletStatementId(financialResult.getStatementId());
        internetPackageLog.setWalletTrackDate(fromISO8601UTC(financialResult.getDate()));
        internetPackageLog.setWalletTrackId(financialResult.getTrackId());
        internetPackageLog.setWalletOrderId(financialResult.getOrderId());

        cash.put(internetPackageLogRepository.save(internetPackageLog));
    }

    @Override
    protected ResultVO onFailWallet(ParameterModel<InternetPackageEntranceVO, InternetPackageLogEntity> param) {
        DCash<InternetPackageLogEntity> cash = param.getCash();

        InternetPackageLogEntity internetPackageLog = cash.get();

        InternetPackageEntranceVO internetPackageEntrance = param.getInputRequest();

        internetPackageLog.setFlowStatus(FlowStatus.FAIL);
        internetPackageLog.setCulprit(filterCulprit);

        setEntity(internetPackageLog, internetPackageEntrance);

        internetPackageLogRepository.save(internetPackageLog);

        return param.getResult();
    }

    private void setEntity(InternetPackageLogEntity internetPackageLog, InternetPackageEntranceVO internetPackageEntrance) {
        internetPackageLog.setChTopupServiceCode(internetPackageEntrance.getChTopupServiceCode());
        internetPackageLog.setMobileNo(internetPackageEntrance.getMobileNo());
        internetPackageLog.setWalletMobileNo(internetPackageEntrance.getWalletMobileNo());
    }

}

