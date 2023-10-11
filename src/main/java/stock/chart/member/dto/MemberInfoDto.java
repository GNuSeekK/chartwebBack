package stock.chart.member.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
public class MemberInfoDto {

    private Long id;
    private String email;
    private String nickname;

    public MemberInfoDto(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
