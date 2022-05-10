package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class FinancialResultVO {
    private String statementId;
    private String trackId;
    private String type;
    private BigDecimal amount;
    private String orderId;
    private Integer status;
    private String date;
    private String desc;
}