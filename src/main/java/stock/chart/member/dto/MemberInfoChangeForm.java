package stock.chart.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberInfoChangeForm {
    @NotBlank
    private String nickname;
}
