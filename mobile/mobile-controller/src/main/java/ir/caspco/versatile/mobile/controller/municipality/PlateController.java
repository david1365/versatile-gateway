package ir.caspco.versatile.mobile.controller.municipality;

import ir.caspco.versatile.context.validation.groups.DInsert;
import ir.caspco.versatile.context.validation.groups.DUpdate;
import ir.caspco.versatile.mobile.context.services.municipality.PlateService;
import ir.caspco.versatile.mobile.context.vo.municipality.ByCustomerNumberVO;
import ir.caspco.versatile.mobile.context.vo.municipality.ByIdVO;
import ir.caspco.versatile.mobile.context.vo.municipality.PlateVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@RestController
//TODO from davood akbari: Find the best solution for Context Path
@RequestMapping("${mobile.full.prefix}/municipal/plate")
public class PlateController implements PlateService {

    private final PlateService plateService;


    public PlateController(PlateService plateService) {
        this.plateService = plateService;
    }


    @PostMapping("/save")
    @Override
    public PlateVO save(@RequestBody @Validated(DInsert.class) @NotNull @Valid PlateVO plateIn) {

        return plateService.save(plateIn);
    }

    @PostMapping("/update")
    @Override
    public PlateVO update(@RequestBody @Validated(DUpdate.class) @NotNull @Valid PlateVO plateIn) {

        return plateService.update(plateIn);
    }

    @PostMapping("/deleteById")
    @Override
    public Boolean deleteById(@RequestBody @NotNull @Valid ByIdVO byId) {

        return plateService.deleteById(byId);
    }

    @PostMapping("/findByCustomerNo")
    @Override
    public List<PlateVO> findByCustomerNo(@RequestBody @Valid @NotNull ByCustomerNumberVO findByMobileNo) {

        return plateService.findByCustomerNo(findByMobileNo);
    }

}


