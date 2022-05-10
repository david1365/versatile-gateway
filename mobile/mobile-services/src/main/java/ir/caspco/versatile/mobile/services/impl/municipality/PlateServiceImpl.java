package ir.caspco.versatile.mobile.services.impl.municipality;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.mobile.context.domains.municipality.PlateEntity;
import ir.caspco.versatile.mobile.context.exceptions.municipality.PlateNotFoundException;
import ir.caspco.versatile.mobile.context.repositories.municipality.PlateRepository;
import ir.caspco.versatile.mobile.context.services.municipality.PlateService;
import ir.caspco.versatile.mobile.context.vo.municipality.ByCustomerNumberVO;
import ir.caspco.versatile.mobile.context.vo.municipality.ByIdVO;
import ir.caspco.versatile.mobile.context.vo.municipality.PlateVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Transactional
public class PlateServiceImpl implements PlateService {

    private final PlateRepository plateRepository;


    public PlateServiceImpl(PlateRepository plateRepository) {
        this.plateRepository = plateRepository;
    }


    @Override
    public PlateVO save(PlateVO plateIn) {

        PlateEntity plate = Shift.just(plateIn).toShift(PlateEntity.class).toObject();
        plate = plateRepository.save(plate);

        return convertToPlateVO(plate);
    }

    @Override
    public PlateVO update(PlateVO plateIn) {

        Optional<PlateEntity> OptionalPlate = plateRepository.findById(plateIn.getId());

        return OptionalPlate
                .map(plateEntity -> {

                    plateEntity.setDescription(plateIn.getDescription());
                    plateEntity.setPlate(plateIn.getPlate());

                    PlateEntity plate = plateRepository.save(plateEntity);

                    return convertToPlateVO(plate);
                })
                .orElseThrow(PlateNotFoundException::new);
    }

    @Override
    public Boolean deleteById(ByIdVO byId) {

        plateRepository.deleteById(byId.getId());

        return true;
    }

    @Override
    public List<PlateVO> findByCustomerNo(ByCustomerNumberVO findByMobileNo) {

        List<PlateEntity> plateEntities = plateRepository.findByCustomerNumber(findByMobileNo.getCustomerNumber());

        return plateEntities.stream()
                .map(this::convertToPlateVO)
                .collect(Collectors.toList());

    }

    private PlateVO convertToPlateVO(PlateEntity plate) {
        return Shift.just(plate).toShift(PlateVO.class).toObject();
    }
}
