package ir.caspco.versatile.mobile.log.services.impl;

import ir.caspco.versatile.common.util.Shift;
import ir.caspco.versatile.mobile.log.domains.ReportLogEntity;
import ir.caspco.versatile.mobile.log.repositories.ReportLogRepository;
import ir.caspco.versatile.mobile.log.services.ReportLogService;
import ir.caspco.versatile.mobile.log.vo.CustomParametersVO;
import ir.caspco.versatile.mobile.log.vo.ReportLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Slf4j
@Service
public class ReportLogServiceImpl implements ReportLogService {

    private final ReportLogRepository reportLogRepository;


    public ReportLogServiceImpl(ReportLogRepository reportLogRepository) {
        this.reportLogRepository = reportLogRepository;
    }

    @Override
    public void report(ReportLogVO reportLogIn) {

        log.error(Shift.just(reportLogIn).toJson());

        String error = reportLogIn.getError();
        String stackTrace = reportLogIn.getStackTrace();
        String deviceParameters = Optional.ofNullable(reportLogIn.getDeviceParameters()).map(o -> Shift.just(o).toJson()).orElse(null);
        String applicationParameters = Optional.ofNullable(reportLogIn.getApplicationParameters()).map(o -> Shift.just(o).toJson()).orElse(null);
        String errorDetails = Optional.ofNullable(reportLogIn.getErrorDetails()).map(o -> Shift.just(o).toJson()).orElse(null);
        String platformType = Optional.ofNullable(reportLogIn.getPlatformType()).map(o -> Shift.just(o).toJson()).orElse(null);

        Timestamp dateTime = reportLogIn.getDateTime();

        CustomParametersVO customParameters = Optional.ofNullable(reportLogIn.getCustomParameters()).orElse(CustomParametersVO.builder().build());

        ReportLogEntity reportLog = ReportLogEntity.builder()
                .error(error)
                .stackTrace(stackTrace)
                .applicationVersion(customParameters.getApplicationVersion())
                .mobileNumber(customParameters.getMobileNumber())
                .username(customParameters.getUsername())
                .customerNumber(customParameters.getCustomerNumber())
                .dateTime(dateTime)
                .deviceParameters(deviceParameters)
                .applicationParameters(applicationParameters)
                .errorDetails(errorDetails)
                .platformType(platformType)
                .build();

        reportLogRepository.save(reportLog);

    }

}
