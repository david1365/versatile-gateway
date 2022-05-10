package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class WalletStatementsVO extends WalletVO {

    @NotNull
    private Date fromDate;

    @NotNull
    private Date toDate;

    @NotNull
    private Integer length;

    @NotNull
    private Integer offset;
}
