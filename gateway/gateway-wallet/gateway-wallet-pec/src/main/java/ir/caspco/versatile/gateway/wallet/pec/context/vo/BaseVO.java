package ir.caspco.versatile.gateway.wallet.pec.context.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseVO {
    @JsonProperty("CorporationPIN")
    protected String corporationPIN;
}
