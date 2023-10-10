package stock.chart.login.dto;

import lombok.Data;

@Data
public class LoginMemberRequestDto {

    private String memberEmail;
    private String password;
}
