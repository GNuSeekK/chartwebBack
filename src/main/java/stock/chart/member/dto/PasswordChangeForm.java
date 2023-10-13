package stock.chart.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordChangeForm {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirm;

}
