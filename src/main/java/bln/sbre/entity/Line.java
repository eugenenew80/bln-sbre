package bln.sbre.entity;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of= {"id"})
@Entity
@Table(name = "res_daily_schedules_lines")
@Immutable
public class Line {
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "header_id")
    private Header header;
}
