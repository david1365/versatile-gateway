package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class ChargeResultVO {
    private String trackingNo;
    private String transactionNumber;
    private TotalBalanceVO totalBalance;
    private LedgerBalanceVO ledgerBalance;
    private Date transactionDate;
}

