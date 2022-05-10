package ir.caspco.versatile.gateway.city.services.municipality.context.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class MunicipalityPlateVO {

    @JsonProperty("Plate")
    @NotNull
    private Integer plate;

    private String mobile;
    private Integer page;
    private Integer pageSize;
    private String sortBy;
    private Boolean isAscending;
    private Integer productId;

}
