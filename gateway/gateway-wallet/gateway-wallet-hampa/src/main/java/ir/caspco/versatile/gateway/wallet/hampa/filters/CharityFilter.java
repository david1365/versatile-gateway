package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.context.validation.DValidator;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.domains.CharityLogEntity;
import ir.caspco.versatile.gateway.wallet.hampa.context.model.ParameterModel;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.FinancialResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.PinChargeEntranceVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.PinChargeResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.ResultVO;
import ir.caspco.versatile.gateway.wallet.hampa.repositories.CharityLogRepository;
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
@Path("/charity")
@SuppressWarnings("unused")
public class CharityFilter extends WithdrawsFilter<PinChargeEntranceVO, PinChargeResultVO, CharityLogEntity> {

    private final CharityLogRepository charityLogRepository;

    public CharityFilter(HampaProperties hampaProperties,
                         PurchaseClient purchaseClient,
                         CharityLogRepository charityLogRepository,
                         DValidator dValidator) {

        super(hampaProperties, purchaseClient, dValidator);

        this.charityLogRepository = charityLogRepository;

    }

    @Override
    protected PurchaseRequestVO beforePurchase(ParameterModel<PinChargeEntranceVO, CharityLogEntity> param) {

        DCash<CharityLogEntity> cash = param.getCash();

        CharityLogEntity charityLog = cash.get();

        PinChargeEntranceVO pinChargeEntrance = param.getInputRequest();

        int trackingNumber = getTrackingNumber(charityLog.getId());

        cash.put(charityLog);

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
    protected PinChargeResultVO afterPurchase(ParameterModel<PinChargeEntranceVO, CharityLogEntity> param, PurchaseResponse purchaseResponse) {

        ThirdPartyResponseVO thirdPartyResponse = purchaseResponse.getThirdPartyResponse();

        return PinChargeResultVO.builder()
                .trackingNumber(thirdPartyResponse.getReference())
                .transactionDate(thirdPartyResponse.getPaymentDate())
                .build();

    }

    @Override
    protected void onSuccessPurchase(ParameterModel<PinChargeEntranceVO, CharityLogEntity> param, PinChargeResultVO result) {
        DCash<CharityLogEntity> cash = param.getCash();

        CharityLogEntity charityLog = cash.get();

        PinChargeEntranceVO pinChargeEntrance = param.getInputRequest();


        setEntity(charityLog, pinChargeEntrance);

        charityLog.setFlowStatus(FlowStatus.DONE);
        charityLog.setTransactionDate(result.getTransactionDate());
        charityLog.setTrackingNumber(result.getTrackingNumber());

        charityLogRepository.save(charityLog);
    }

    @Override
    protected void onFailPurchase(ParameterModel<PinChargeEntranceVO, CharityLogEntity> param) {
        DCash<CharityLogEntity> cash = param.getCash();

        CharityLogEntity charityLog = cash.get();

        PinChargeEntranceVO pinChargeEntrance = param.getInputRequest();


        setEntity(charityLog, pinChargeEntrance);

        charityLog.setFlowStatus(FlowStatus.FAIL);
        charityLog.setCulprit(clientCulprit);

        charityLogRepository.save(charityLog);
    }

    @Override
    protected void doSuccessWallet(ParameterModel<PinChargeEntranceVO, CharityLogEntity> param, FinancialResultVO financialResult) {
        DCash<CharityLogEntity> cash = param.getCash();

        CharityLogEntity charityLog = cash.get();
        charityLog.setWalletStatementId(financialResult.getStatementId());
        charityLog.setWalletTrackDate(fromISO8601UTC(financialResult.getDate()));
        charityLog.setWalletTrackId(financialResult.getTrackId());
        charityLog.setWalletOrderId(financialResult.getOrderId());

        cash.put(charityLogRepository.save(charityLog));
    }


    @Override
    protected void onCreate(PinChargeEntranceVO input, UUID uuid, DCash<CharityLogEntity> cash) {
        CharityLogEntity charityLog = CharityLogEntity.builder()
                .walletId(input.getWalletId())
                .amount(input.getAmount())
                .flowStatus(FlowStatus.CREATION)
                .requestId(cash.requestId())
                .trackingNoEntrance(uuid.toString())
                .build();

        cash.put(charityLogRepository.save(charityLog));
    }

    @Override
    protected ResultVO onFailWallet(ParameterModel<PinChargeEntranceVO, CharityLogEntity> param) {
        DCash<CharityLogEntity> cash = param.getCash();

        CharityLogEntity charityLog = cash.get();

        PinChargeEntranceVO pinChargeEntrance = param.getInputRequest();

        charityLog.setFlowStatus(FlowStatus.FAIL);
        charityLog.setCulprit(filterCulprit);

        setEntity(charityLog, pinChargeEntrance);

        charityLogRepository.save(charityLog);

        return param.getResult();
    }

    private void setEntity(CharityLogEntity charityLog, PinChargeEntranceVO pinChargeEntrance) {
        charityLog.setPaymentServiceId(pinChargeEntrance.getPaymentServiceId());
        charityLog.setWalletMobileNo(pinChargeEntrance.getWalletMobileNo());
    }

}

