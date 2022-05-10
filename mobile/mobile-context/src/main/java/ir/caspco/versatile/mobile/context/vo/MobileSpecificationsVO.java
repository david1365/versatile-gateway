package ir.caspco.versatile.mobile.context.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@ToString
public class MobileSpecificationsVO {

    private String code;
    private String mobileBrand;
    private String mobileModel;
    private String androidVersion;
    private String appVersion;
    private String nationalCode;
    private String phoneNumber;
}
