package bln.sbre.client.query;

import lombok.Data;
import java.util.List;

@Data
public class QueryFailResponseDto {
    private boolean success;
    private List<String> errors;
}
