package bln.sbre.entity;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(of= {"code"})
@Entity
@Table(name = "bems_schedules_errors")
@Immutable
public class PlanError {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
