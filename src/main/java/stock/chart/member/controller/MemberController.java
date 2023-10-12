package stock.chart.member.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.chart.member.dto.DeleteMemberForm;
import stock.chart.member.dto.MemberInfoDto;
import stock.chart.member.dto.SignUpForm;
import stock.chart.member.exception.DuplicateMemberException;
import stock.chart.member.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public Object getMemberInfo(@RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("토큰이 존재하지 않습니다.");
        }
        if (!accessToken.startsWith("Bearer ")) {
            throw new RuntimeException("토큰이 올바르지 않습니다.");
        }
        accessToken = accessToken.substring(7);
        return memberService.getMemberInfo(accessToken);
    }

    /**
     * 로그인 폼 확인 필요하면 400, 중복일 경우 409, 성공시 200
     */
    @PostMapping("/register")
    public ResponseEntity registerMember(@Valid @RequestBody SignUpForm signUpForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        MemberInfoDto memberInfoDto;
        try {
            Long id = memberService.registerMember(signUpForm);
            memberInfoDto = memberService.getMemberInfo(id);
        } catch (DuplicateMemberException e) {
            bindingResult.addError(e.getFieldError());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok().body(memberInfoDto);
    }

    /**
     * 성공 204, 403 비밀번호 불일치, 400 폼 입력 에러, 404 존재하지 않는 회원
     */
    @PostMapping("/delete")
    public ResponseEntity deleteMember(@Valid @RequestBody DeleteMemberForm deleteMemberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        try {
            memberService.deleteMember(deleteMemberForm);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("password")) {
                bindingResult.addError(new FieldError("deleteMemberForm", "password", "비밀번호가 일치하지 않습니다."));
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(bindingResult.getAllErrors());
            }
            if (e.getMessage().equals("존재하지 않는 회원입니다.")) {
                bindingResult.addError(new ObjectError("deleteMemberForm", "존재하지 않는 회원입니다."));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bindingResult.getAllErrors());
            }
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
