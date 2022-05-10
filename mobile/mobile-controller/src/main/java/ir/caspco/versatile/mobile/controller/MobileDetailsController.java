package ir.caspco.versatile.mobile.controller;

import ir.caspco.versatile.mobile.context.services.MobileDetailsService;
import ir.caspco.versatile.mobile.context.vo.MobileSpecificationsVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
@RequestMapping("${mobile.full.prefix}")
public class MobileDetailsController implements MobileDetailsService {

    private final MobileDetailsService mobileDetailsService;


    public MobileDetailsController(MobileDetailsService mobileDetailsService) {
        this.mobileDetailsService = mobileDetailsService;
    }

    @Override
    @PostMapping("/havingMobileDetails")
    public boolean havingMobileDetails(@RequestBody @Valid MobileSpecificationsVO mobileSpecifications) {
        return mobileDetailsService.havingMobileDetails(mobileSpecifications);
    }
}
