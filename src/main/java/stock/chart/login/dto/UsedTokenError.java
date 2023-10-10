package stock.chart.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsedTokenError {

    private String error;
    private String code;
    private String message;

}
