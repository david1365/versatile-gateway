package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.context.validation.DValidator;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.domains.BillPaymentLogEntity;
import ir.caspco.versatile.gateway.wallet.hampa.context.model.ParameterModel;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.*;
import ir.caspco.versatile.gateway.wallet.hampa.repositories.BillPaymentLogRepository;
import ir.caspco.versatile.jms.client.common.client.PurchaseClient;
import ir.caspco.versatile.jms.client.common.msg.PurchaseResponse;
import ir.caspco.versatile.jms.client.common.vo.BillCriteriaVO;
import ir.caspco.versatile.jms.client.common.vo.PayedBillInformationVO;
import ir.caspco.versatile.jms.client.common.vo.PurchaseRequestVO;

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
@Path("/billPayment")
@SuppressWarnings("unused")
public class BillPaymentFilter extends WithdrawsFilter<BillPaymentEntranceVO, BillPaymentResultVO, BillPaymentLogEntity> {

    private final BillPaymentLogRepository billPaymentLogRepository;


    public BillPaymentFilter(HampaProperties hampaProperties,
                             PurchaseClient purchaseClient,
                             BillPaymentLogRepository billPaymentLogRepository,
                             DValidator dValidator) {

        super(hampaProperties, purchaseClient, dValidator);

        this.billPaymentLogRepository = billPaymentLogRepository;

    }

    @Override
    protected PurchaseRequestVO beforePurchase(ParameterModel<BillPaymentEntranceVO, BillPaymentLogEntity> param) {
        DCash<BillPaymentLogEntity> cash = param.getCash();

        BillPaymentLogEntity billPaymentLog = cash.get();

        int trackingNumber = getTrackingNumber(billPaymentLog.getId());

        BillPaymentEntranceVO billPaymentEntrance = param.getInputRequest();

        cash.put(billPaymentLog);


        return PurchaseRequestVO.builder()

                .billCriteria(
                        BillCriteriaVO.builder()
                                .walletId(billPaymentEntrance.getWalletId())
                                .payId(billPaymentEntrance.getPayId())
                                .billId(billPaymentEntrance.getBillId())
                                .walletMobileNo(Long.valueOf(billPaymentEntrance.getWalletMobileNo()))
                                .trackingNumber(trackingNumber)
                                .channelType(billPaymentEntrance.getChannelType().value())
                                .build()
                )
                .walletThirdPartyConfig(getDefaultThirdPartyConfig())
                .isBillPayment(true)
                .build();
    }

    @Override
    protected BillPaymentResultVO afterPurchase(ParameterModel<BillPaymentEntranceVO, BillPaymentLogEntity> param, PurchaseResponse purchaseResponse) {

        PayedBillInformationVO payedBillInformation = purchaseResponse.getPayedBillInformation();


        return BillPaymentResultVO.builder()
                .cardOwner(CardOwnerVO.builder().build())
                .billId(payedBillInformation.getBillId())
                .billTitle(payedBillInformation.getBillTitle())
                .billType(payedBillInformation.getBillType())
                .referralNumber(payedBillInformation.getReference())
                .date(payedBillInformation.getPaymentDate())
                .payId(payedBillInformation.getBillPayment())
                .build();

    }

    @Override
    protected void onSuccessPurchase(ParameterModel<BillPaymentEntranceVO, BillPaymentLogEntity> param, BillPaymentResultVO result) {
        DCash<BillPaymentLogEntity> cash = param.getCash();

        BillPaymentLogEntity billPaymentLog = cash.get();

        BillPaymentEntranceVO billPaymentEntrance = param.getInputRequest();


        setEntity(billPaymentLog, billPaymentEntrance);

        billPaymentLog.setBillForeignTitle(result.getBillForeignTitle());
        billPaymentLog.setFlowStatus(FlowStatus.DONE);
        billPaymentLog.setBillTitle(result.getBillTitle());
        billPaymentLog.setBillId(result.getBillId());
        billPaymentLog.setCycle(result.getCycle());
        billPaymentLog.setDateTime(result.getDate());
        billPaymentLog.setBillType(result.getBillType());
        billPaymentLog.setFileCode(result.getFileCode());
        billPaymentLog.setPayId(result.getPayId());
        billPaymentLog.setReferralNumber(result.getReferralNumber());

        billPaymentLogRepository.save(billPaymentLog);
    }

    @Override
    protected void onFailPurchase(ParameterModel<BillPaymentEntranceVO, BillPaymentLogEntity> param) {
        DCash<BillPaymentLogEntity> cash = param.getCash();

        BillPaymentLogEntity billPaymentLog = cash.get();

        BillPaymentEntranceVO billPaymentEntrance = param.getInputRequest();


        setEntity(billPaymentLog, billPaymentEntrance);

        billPaymentLog.setFlowStatus(FlowStatus.FAIL);
        billPaymentLog.setCulprit(clientCulprit);

        billPaymentLogRepository.save(billPaymentLog);
    }

    @Override
    protected void doSuccessWallet(ParameterModel<BillPaymentEntranceVO, BillPaymentLogEntity> param, FinancialResultVO financialResult) {
        DCash<BillPaymentLogEntity> cash = param.getCash();

        BillPaymentLogEntity billPaymentLog = cash.get();
        billPaymentLog.setWalletStatementId(financialResult.getStatementId());
        billPaymentLog.setWalletTrackDate(fromISO8601UTC(financialResult.getDate()));
        billPaymentLog.setWalletTrackId(financialResult.getTrackId());
        billPaymentLog.setWalletOrderId(financialResult.getOrderId());

        cash.put(billPaymentLogRepository.save(billPaymentLog));
    }


    @Override
    protected void onCreate(BillPaymentEntranceVO input, UUID uuid, DCash<BillPaymentLogEntity> cash) {
        BillPaymentLogEntity billPaymentLog = BillPaymentLogEntity.builder()
                .walletId(input.getWalletId())
                .amount(input.getAmount())
                .flowStatus(FlowStatus.CREATION)
                .requestId(cash.requestId())
                .trackingNoEntrance(uuid.toString())
                .build();

        cash.put(billPaymentLogRepository.save(billPaymentLog));
    }

    @Override
    protected ResultVO onFailWallet(ParameterModel<BillPaymentEntranceVO, BillPaymentLogEntity> param) {
        DCash<BillPaymentLogEntity> cash = param.getCash();

        BillPaymentLogEntity billPaymentLog = cash.get();

        BillPaymentEntranceVO billPaymentEntrance = param.getInputRequest();

        billPaymentLog.setFlowStatus(FlowStatus.FAIL);
        billPaymentLog.setCulprit(filterCulprit);

        setEntity(billPaymentLog, billPaymentEntrance);

        billPaymentLogRepository.save(billPaymentLog);

        return param.getResult();
    }

    private void setEntity(BillPaymentLogEntity billPaymentLog, BillPaymentEntranceVO billPaymentEntrance) {
        billPaymentLog.setWalletMobileNo(billPaymentEntrance.getWalletMobileNo());
    }
}

