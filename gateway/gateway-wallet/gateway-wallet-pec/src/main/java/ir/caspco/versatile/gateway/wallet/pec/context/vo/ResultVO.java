package ir.caspco.versatile.gateway.wallet.pec.context.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
public class ResultVO {
    @JsonProperty("Keys")
    private KeysVO keys;
}

