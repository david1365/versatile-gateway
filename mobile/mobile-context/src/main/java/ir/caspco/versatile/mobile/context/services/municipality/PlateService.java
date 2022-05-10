package ir.caspco.versatile.mobile.context.services.municipality;


import ir.caspco.versatile.mobile.context.vo.municipality.ByCustomerNumberVO;
import ir.caspco.versatile.mobile.context.vo.municipality.ByIdVO;
import ir.caspco.versatile.mobile.context.vo.municipality.PlateVO;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface PlateService {
    PlateVO save(PlateVO plateIn);

    PlateVO update(PlateVO plateIn);

    Boolean deleteById(ByIdVO byId);

    List<PlateVO> findByCustomerNo(ByCustomerNumberVO byCustomerNumberVO);
}
