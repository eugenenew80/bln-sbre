package bln.sbre.entity;

import bln.sbre.entity.enums.BatchStatusEnum;
import bln.sbre.entity.enums.OperationEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of= {"id"})
@Entity
@Table(name = "res_ds_lines_interface")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = "LineInterface.updateStatuses",
        procedureName = "bems_interface_pkg.update_statuses",
        parameters={
            @StoredProcedureParameter(name="p_header_id", type=Long.class, mode=ParameterMode.IN),
        }
    )
})
public class LineInterface {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ds_line_id")
    private Line line;

    @Column(name = "plan_date")
    private LocalDate planDate;

    @Column(name = "sender")
    private String sender;

    @Column(name = "partner")
    private String partner;

    @Column(name = "operation_code")
    @Enumerated(EnumType.STRING)
    private OperationEnum operation;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BatchStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "error_code")
    private PlanError error;

    @Column(name = "bems_plan_id")
    private Long externalId;

    @Column(name = "create_by")
    private Long createdBy;

    @Column(name = "create_date")
    private LocalDateTime createdDate;

    @Column(name = "last_update_by")
    private Long lastUpdatedBy;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdatedDate;
}
