package ir.caspco.versatile.mobile.log.controller;

import ir.caspco.versatile.mobile.log.services.ReportLogService;
import ir.caspco.versatile.mobile.log.vo.ReportLogVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */
@RestController
//TODO from davood akbari: Find the best solution for Context Path
@RequestMapping("${mobile.log.prefix}/mobile")
public class ReportLogController {

    private final ReportLogService reportLogService;


    public ReportLogController(ReportLogService reportLogService) {
        this.reportLogService = reportLogService;
    }

    @PostMapping("/reportLog")
    public void reportLog(@RequestBody ReportLogVO reportLog) {

        reportLogService.report(reportLog);

    }

}

