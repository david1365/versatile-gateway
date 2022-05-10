package ir.caspco.versatile.mobile.log.domains;

import ir.caspco.versatile.context.domains.AuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

import static ir.caspco.versatile.context.domains.Schema.MOBILE;

/**
 * @author Davood Akbari - 1399
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

@Data
@SuperBuilder
@Entity
@Table(name = ReportLogEntity.TABLE, schema = MOBILE)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReportLogEntity extends AuditEntity {

    public static final String TABLE = "MOBILE_REPORT_LOG";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = MOBILE + ".SQ_" + ReportLogEntity.TABLE + "_ID")
    private BigDecimal id;

    private String applicationVersion;

    private String mobileNumber;

    private String username;

    private String customerNumber;

    @Lob
    @Column(name = "CURRENT_ERROR")
    private String error;

    @Lob
    private String stackTrace;

    private Timestamp dateTime;

    @Lob
    private String deviceParameters;

    @Lob
    private String applicationParameters;

    @Lob
    private String errorDetails;

    @Lob
    private String platformType;

}
