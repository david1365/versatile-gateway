package ir.caspco.versatile.gateway.wallet.hampa.filters;

import ir.caspco.versatile.context.enums.FlowStatus;
import ir.caspco.versatile.context.validation.DValidator;
import ir.caspco.versatile.gateway.smart.filters.annotations.Filter;
import ir.caspco.versatile.gateway.smart.filters.annotations.Path;
import ir.caspco.versatile.gateway.smart.filters.cash.DCash;
import ir.caspco.versatile.gateway.wallet.hampa.context.domains.InsurancePaymentLogEntity;
import ir.caspco.versatile.gateway.wallet.hampa.context.model.ParameterModel;
import ir.caspco.versatile.gateway.wallet.hampa.context.properties.HampaProperties;
import ir.caspco.versatile.gateway.wallet.hampa.context.vo.*;
import ir.caspco.versatile.gateway.wallet.hampa.repositories.InsurancePaymentLogRepository;
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
@Path("/insurancePayment")
@SuppressWarnings("unused")
public class InsurancePaymentFilter extends WithdrawsFilter<BillPaymentEntranceVO, BillPaymentResultVO, InsurancePaymentLogEntity> {

    private final InsurancePaymentLogRepository insurancePaymentLogRepository;


    public InsurancePaymentFilter(HampaProperties hampaProperties,
                                  PurchaseClient purchaseClient,
                                  InsurancePaymentLogRepository insurancePaymentLogRepository,
                                  DValidator dValidator) {

        super(hampaProperties, purchaseClient, dValidator);

        this.insurancePaymentLogRepository = insurancePaymentLogRepository;

    }

    @Override
    protected PurchaseRequestVO beforePurchase(ParameterModel<BillPaymentEntranceVO, InsurancePaymentLogEntity> param) {

        DCash<InsurancePaymentLogEntity> cash = param.getCash();

        InsurancePaymentLogEntity insurancePaymentLog = cash.get();

        BillPaymentEntranceVO billPaymentEntrance = param.getInputRequest();

        int trackingNumber = getTrackingNumber(insurancePaymentLog.getId());

        cash.put(insurancePaymentLog);


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
    protected BillPaymentResultVO afterPurchase(ParameterModel<BillPaymentEntranceVO, InsurancePaymentLogEntity> param,
                                                PurchaseResponse purchaseResponse) {

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
    protected void onSuccessPurchase(ParameterModel<BillPaymentEntranceVO, InsurancePaymentLogEntity> param, BillPaymentResultVO result) {
        DCash<InsurancePaymentLogEntity> cash = param.getCash();

        InsurancePaymentLogEntity insurancePaymentLog = cash.get();

        BillPaymentEntranceVO billPaymentEntrance = param.getInputRequest();


        setEntity(insurancePaymentLog, billPaymentEntrance);

        insurancePaymentLog.setFlowStatus(FlowStatus.DONE);
        insurancePaymentLog.setBillForeignTitle(result.getBillForeignTitle());
        insurancePaymentLog.setBillId(result.getBillId());
        insurancePaymentLog.setBillTitle(result.getBillTitle());
        insurancePaymentLog.setBillType(result.getBillType());
        insurancePaymentLog.setCycle(result.getCycle());
        insurancePaymentLog.setDateTime(result.getDate());
        insurancePaymentLog.setFileCode(result.getFileCode());
        insurancePaymentLog.setPayId(result.getPayId());
        insurancePaymentLog.setReferralNumber(result.getReferralNumber());

        insurancePaymentLogRepository.save(insurancePaymentLog);
    }

    @Override
    protected void onFailPurchase(ParameterModel<BillPaymentEntranceVO, InsurancePaymentLogEntity> param) {
        DCash<InsurancePaymentLogEntity> cash = param.getCash();

        InsurancePaymentLogEntity insurancePaymentLog = cash.get();

        BillPaymentEntranceVO billPaymentEntrance = param.getInputRequest();


        setEntity(insurancePaymentLog, billPaymentEntrance);

        insurancePaymentLog.setFlowStatus(FlowStatus.FAIL);
        insurancePaymentLog.setCulprit(clientCulprit);

        insurancePaymentLogRepository.save(insurancePaymentLog);
    }

    @Override
    protected void doSuccessWallet(ParameterModel<BillPaymentEntranceVO, InsurancePaymentLogEntity> param, FinancialResultVO financialResult) {
        DCash<InsurancePaymentLogEntity> cash = param.getCash();

        InsurancePaymentLogEntity insurancePaymentLog = cash.get();
        insurancePaymentLog.setWalletStatementId(financialResult.getStatementId());
        insurancePaymentLog.setWalletTrackDate(fromISO8601UTC(financialResult.getDate()));
        insurancePaymentLog.setWalletTrackId(financialResult.getTrackId());
        insurancePaymentLog.setWalletOrderId(financialResult.getOrderId());

        cash.put(insurancePaymentLogRepository.save(insurancePaymentLog));
    }


    @Override
    protected void onCreate(BillPaymentEntranceVO input, UUID uuid, DCash<InsurancePaymentLogEntity> cash) {
        InsurancePaymentLogEntity insurancePaymentLog = InsurancePaymentLogEntity.builder()
                .walletId(input.getWalletId())
                .amount(input.getAmount())
                .flowStatus(FlowStatus.CREATION)
                .requestId(cash.requestId())
                .trackingNoEntrance(uuid.toString())
                .build();

        cash.put(insurancePaymentLogRepository.save(insurancePaymentLog));
    }

    @Override
    protected ResultVO onFailWallet(ParameterModel<BillPaymentEntranceVO, InsurancePaymentLogEntity> param) {
        DCash<InsurancePaymentLogEntity> cash = param.getCash();

        InsurancePaymentLogEntity insurancePaymentLog = cash.get();

        BillPaymentEntranceVO billPaymentEntrance = param.getInputRequest();

        insurancePaymentLog.setFlowStatus(FlowStatus.FAIL);
        insurancePaymentLog.setCulprit(filterCulprit);

        setEntity(insurancePaymentLog, billPaymentEntrance);

        insurancePaymentLogRepository.save(insurancePaymentLog);

        return param.getResult();
    }

    private void setEntity(InsurancePaymentLogEntity insurancePaymentLog, BillPaymentEntranceVO billPaymentEntrance) {
        insurancePaymentLog.setWalletMobileNo(billPaymentEntrance.getWalletMobileNo());
    }

}

