package ir.caspco.versatile.mobile.services.impl;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.mobile.context.domains.MobileSpecificationsEntity;
import ir.caspco.versatile.mobile.context.domains.MobileSpecificationsLogEntity;
import ir.caspco.versatile.mobile.context.repositories.MobileSpecificationsLogRepository;
import ir.caspco.versatile.mobile.context.repositories.MobileSpecificationsRepository;
import ir.caspco.versatile.mobile.context.services.MobileDetailsService;
import ir.caspco.versatile.mobile.context.vo.MobileSpecificationsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Service
@Slf4j
public class MobileDetailsServiceImpl implements MobileDetailsService {

    private final MobileSpecificationsRepository mobileSpecificationsRepository;
    private final MobileSpecificationsLogRepository mobileSpecificationsLogRepository;

    private List<MobileSpecificationsEntity> mobileSpecificationsCash;


    public MobileDetailsServiceImpl(MobileSpecificationsRepository mobileSpecificationsRepository, MobileSpecificationsLogRepository mobileSpecificationsLogRepository) {
        this.mobileSpecificationsRepository = mobileSpecificationsRepository;
        this.mobileSpecificationsLogRepository = mobileSpecificationsLogRepository;
    }

    @PostConstruct
    private void init() {

        this.mobileSpecificationsCash = (List<MobileSpecificationsEntity>) mobileSpecificationsRepository.findAll();
    }


    @Override
    public boolean havingMobileDetails(MobileSpecificationsVO mobileSpecifications) {

        MobileSpecificationsLogEntity mobileSpecificationsLog = Shift.just(mobileSpecifications)
                .toShift(MobileSpecificationsLogEntity.class)
                .toObject();


        final boolean exists = mobileSpecificationsCash.stream()
                .anyMatch(mobileSpecificationCash -> {

                    String mobileBrand = mobileSpecifications.getMobileBrand();
                    if (mobileBrand == null) {
                        return false;
                    }

                    String mobileModel = mobileSpecifications.getMobileModel();
                    if (mobileModel == null) {
                        return false;
                    }

                    return mobileBrand.toLowerCase().trim().contains(mobileSpecificationCash.getMobileBrand().toLowerCase().trim()) &&
                            mobileModel.toLowerCase().trim().contains(mobileSpecificationCash.getMobileModel().toLowerCase().trim());

                });

        mobileSpecificationsLog.setExistsIn(exists);

        log.error(mobileSpecificationsLog.toString());

        mobileSpecificationsLogRepository.save(mobileSpecificationsLog);

        return exists;
    }
}
