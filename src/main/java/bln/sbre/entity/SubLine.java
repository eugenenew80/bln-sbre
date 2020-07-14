package bln.sbre.entity;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(of= {"pos"})
@Entity
@Table(name = "res_daily_schedules_sublines")
@Immutable
public class SubLine {
    @EmbeddedId
    private SubLineId pk;

    @ManyToOne
    @JoinColumn(name = "ds_line_id", insertable = false, updatable = false)
    private Line line;

    @Column(name = "pos", insertable = false, updatable = false)
    private Integer pos;

    @Column(name = "val")
    private Double val;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class SubLineId implements Serializable {
        @Column(name = "ds_line_id")
        private Long lineId;

        @Column(name = "pos")
        private Long pos;
    }
}
