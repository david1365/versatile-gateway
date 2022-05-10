package ir.caspco.versatile.mobile.context.vo.municipality;

import ir.caspco.versatile.common.validation.annotations.IsValidPlate;
import ir.caspco.versatile.context.validation.groups.DInsert;
import ir.caspco.versatile.context.validation.groups.DUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class PlateVO {

    @NotNull(groups = DUpdate.class)
    private BigDecimal id;

    @NotNull(groups = {DInsert.class})
    @NotBlank(groups = {DInsert.class})
    @NotEmpty(groups = {DInsert.class})
    private String customerNumber;

    @NotNull(groups = {DInsert.class, DUpdate.class})
    @IsValidPlate(groups = {DInsert.class, DUpdate.class})
    private Integer plate;

    private String description;

}
