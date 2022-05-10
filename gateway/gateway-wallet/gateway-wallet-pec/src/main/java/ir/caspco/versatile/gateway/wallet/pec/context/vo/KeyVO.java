package ir.caspco.versatile.gateway.wallet.pec.context.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
public class KeyVO {
    @JsonProperty("ReturnId")
    private BigInteger returnId;

    @JsonProperty("ResultId")
    private int resultId;

    @JsonProperty("ResultDesc")
    private String resultDesc;
}
