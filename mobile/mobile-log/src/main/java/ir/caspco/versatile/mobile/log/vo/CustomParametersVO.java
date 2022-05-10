package ir.caspco.versatile.mobile.log.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@NoArgsConstructor
public class CustomParametersVO {
    private String applicationVersion;
    private String mobileNumber;
    private String username;
    private String customerNumber;
}
