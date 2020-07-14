package bln.sbre.entity;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Data
@EqualsAndHashCode(of= {"code"})
@Entity
@Table(name = "bems_operations")
@Immutable
public class Operation {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;
}
