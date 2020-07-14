package bln.sbre.client.query;

import bln.sbre.entity.enums.OperationEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import java.time.LocalDate;

@Data
@JsonPropertyOrder({"planDate", "sender", "partner", "operation", "hour0", "hour1", "hour2", "hour3", "hour4", "hour5", "hour6", "hour7", "hour8", "hour9", "hour10", "hour11", "hour12", "hour13", "hour14", "hour15", "hour16", "hour17", "hour18", "hour19", "hour20", "hour21", "hour22", "hour23"})
public class QueryRequestDto {
    @JsonIgnore
    private Long id;

    @JsonProperty("planDate")
    private LocalDate planDate;

    @JsonProperty("sender")
    private PartnerDto sender;

    @JsonProperty("partner")
    private PartnerDto partner;

    @JsonProperty("operation")
    private OperationEnum operation;

    private Double hour0 = 0d;
    private Double hour1 = 0d;
    private Double hour2 = 0d;
    private Double hour3 = 0d;
    private Double hour4 = 0d;
    private Double hour5 = 0d;
    private Double hour6 = 0d;
    private Double hour7 = 0d;
    private Double hour8 = 0d;
    private Double hour9 = 0d;
    private Double hour10 = 0d;
    private Double hour11 = 0d;
    private Double hour12 = 0d;
    private Double hour13 = 0d;
    private Double hour14 = 0d;
    private Double hour15 = 0d;
    private Double hour16 = 0d;
    private Double hour17 = 0d;
    private Double hour18 = 0d;
    private Double hour19 = 0d;
    private Double hour20 = 0d;
    private Double hour21 = 0d;
    private Double hour22 = 0d;
    private Double hour23 = 0d;
}
