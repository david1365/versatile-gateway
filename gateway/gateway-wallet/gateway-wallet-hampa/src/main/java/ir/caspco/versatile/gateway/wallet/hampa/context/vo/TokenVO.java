package ir.caspco.versatile.gateway.wallet.hampa.context.vo;

import ir.caspco.versatile.common.util.security.HeaderUtil;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
public class TokenVO {

    private String accessToken;

    private Integer exp;

    private Date expireDate;

    private String refreshToken;

    public void setExp(Integer exp) {

        this.exp = exp;

        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, exp);

        expireDate = calendar.getTime();

    }

    public Boolean isExpired() {

        return HeaderUtil.isExpired(expireDate);

    }
}
