package stock.chart.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import stock.chart.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpForm {

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;



    public Member toEntity() {
        return Member.builder()
            .email(email)
            .password(password)
            .nickname(nickname)
            .build();
    }
}
