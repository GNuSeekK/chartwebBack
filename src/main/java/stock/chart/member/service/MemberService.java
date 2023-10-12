package stock.chart.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.domain.Member;
import stock.chart.member.dto.DeleteMemberForm;
import stock.chart.member.dto.MemberInfoDto;
import stock.chart.member.dto.SignUpForm;
import stock.chart.member.exception.DuplicateEmailException;
import stock.chart.member.exception.DuplicateNicknameException;
import stock.chart.member.repository.MemberRepository;
import stock.chart.security.JwtTokenProvider;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberInfoDto getMemberInfo(String accessToken) {
        Long id = Long.valueOf(jwtTokenProvider.getMemberId(accessToken));
        return memberRepository.findMemberDtoById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
    }

    public MemberInfoDto getMemberInfo(Long id) {
        return memberRepository.findMemberDtoById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
    }

    @Transactional
    public Long registerMember(SignUpForm signUpForm) {
        memberRepository.findByEmail(signUpForm.getEmail())
            .ifPresent(member -> {
                throw new DuplicateEmailException();
            });
        memberRepository.findByNickname(signUpForm.getNickname())
            .ifPresent(member -> {
                throw new DuplicateNicknameException();
            });
        Member member = memberRepository.save(signUpForm.toEntity());
        return member.getId();
    }

    @Transactional
    public Long deleteMember(DeleteMemberForm deleteMemberForm) {
        Member member = memberRepository.findByEmail(deleteMemberForm.getEmail())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
        if (!member.getPassword().equals(deleteMemberForm.getPassword())) {
            throw new RuntimeException("password");
        }
        memberRepository.delete(member);
        return member.getId();
    }
}
