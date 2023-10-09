package stock.chart.login.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MemberLoginRequestDto {

    private String memberEmail;
    private String password;
}
