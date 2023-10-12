package stock.chart.member.dto;

import javax.annotation.processing.Generated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteMemberForm {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public DeleteMemberForm(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public DeleteMemberForm() {

    }


}
