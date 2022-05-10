package ir.caspco.versatile.mobile.log.vo;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
public class ReportLogVO {
    private String error;
    private String stackTrace;
    private Timestamp dateTime;
    private Object deviceParameters;
    private Object applicationParameters;
    private CustomParametersVO customParameters;
    private Object errorDetails;
    private Object platformType;
}
