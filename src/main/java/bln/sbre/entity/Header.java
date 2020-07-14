package bln.sbre.entity;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(of= {"id"})
@Entity
@Table(name = "res_daily_schedules_headers")
@Immutable
public class Header {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "schedule_date")
    private LocalDate planDate;
}
