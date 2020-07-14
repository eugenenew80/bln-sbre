package bln.sbre.client.query;

import lombok.Data;
import java.util.List;

@Data
public class QuerySuccessResponseDto {
    private boolean success;
    private List<Long> planIds;
}
