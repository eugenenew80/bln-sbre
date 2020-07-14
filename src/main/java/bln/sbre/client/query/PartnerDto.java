package bln.sbre.client.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartnerDto {

    @JsonProperty("code")
    private String code;
}
