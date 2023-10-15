package stock.chart.member.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.member.dto.DeleteMemberForm;
import stock.chart.member.dto.MemberInfoChangeForm;
import stock.chart.member.dto.MemberInfoDto;
import stock.chart.member.dto.PasswordChangeForm;
import stock.chart.member.dto.SignUpForm;
import stock.chart.member.exception.PasswordNotMatchException;
import stock.chart.member.service.MemberService;
import stock.chart.security.exception.AccessTokenInvalidException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public Object getMemberInfo(@RequestHeader("Authorization") String accessToken) {
        accessToken = accessTokenValidityCheck(accessToken);
        return memberService.getMemberInfo(accessToken);
    }

    /**
     * 회원가입 폼 확인 필요하면 400, 중복일 경우 409, 성공시 200
     */
    @PostMapping
    public ResponseEntity registerMember(@Valid @RequestBody SignUpForm signUpForm, BindingResult bindingResult) {
        log.info("signUpForm : {}", signUpForm);
        MemberInfoDto memberInfoDto;
        Long id = memberService.registerMember(signUpForm);
        memberInfoDto = memberService.getMemberInfo(id);
        return ResponseEntity.ok().body(memberInfoDto);
    }

    /**
     * 성공 204, 403 비밀번호 불일치, 400 폼 입력 에러, 404 존재하지 않는 회원
     */
    @PostMapping("/delete")
    public ResponseEntity deleteMember(@Valid @RequestBody DeleteMemberForm deleteMemberForm,
        BindingResult bindingResult) {
        memberService.deleteMember(deleteMemberForm);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 성공 202, 403 비밀번호 불일치, 400 폼 입력 에러, 404 존재하지 않는 회원
     */
    @PatchMapping("/password")
    public ResponseEntity changePassword(@Valid @RequestBody PasswordChangeForm passwordChangeForm,
        BindingResult bindingResult) {

        if (!passwordChangeForm.getNewPassword().equals(passwordChangeForm.getNewPasswordConfirm())) {
            throw new PasswordNotMatchException();
        }

        memberService.changePassword(passwordChangeForm.getEmail(), passwordChangeForm.getPassword(),
            passwordChangeForm.getNewPassword());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    /**
     * 닉네임 변경은 회원에 막대한 영향 끼치지 않으므로 accessToken만 확인하고 202 반환 만약 닉네임 중복일 경우에는 409 반환
     */
    @PatchMapping("/nickname")
    public ResponseEntity changeNickname(@RequestHeader("Authorization") String accessToken, @RequestBody
    MemberInfoChangeForm memberInfoChangeForm, BindingResult bindingResult) {
        accessToken = accessTokenValidityCheck(accessToken);

        memberService.changeNickname(accessToken, memberInfoChangeForm.getNickname());

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    private String accessTokenValidityCheck(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new AccessTokenInvalidException();
        }
        if (!accessToken.startsWith("Bearer ")) {
            throw new AccessTokenInvalidException();
        }
        return accessToken.substring(7);
    }

}
