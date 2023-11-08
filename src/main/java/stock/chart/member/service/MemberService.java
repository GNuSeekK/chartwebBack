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
import stock.chart.member.exception.InvalidMemberException;
import stock.chart.member.exception.PasswordNotMatchException;
import stock.chart.member.repository.MemberRepository;
import stock.chart.security.JwtTokenProvider;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberInfoDto getMemberInfo(Long id) {
        return memberRepository.findMemberDtoById(id)
            .orElseThrow(InvalidMemberException::new);
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
        Member member = getMemberByEmail(deleteMemberForm.getEmail());
        passwordCheck(deleteMemberForm.getPassword(), member);
        memberRepository.delete(member);
        return member.getId();
    }

    @Transactional
    public Long changePassword(String email, String password, String newPassword) {
        Member member = getMemberByEmail(email);
        passwordCheck(password, member);
        member.changePassword(newPassword);
        return member.getId();
    }



    @Transactional
    public Long changeNickname(Long id, String nickname) {
        Member member = getMemberById(id);
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new DuplicateNicknameException();
        }
        member.changeNickname(nickname);
        return member.getId();
    }

    @Transactional
    public int changePoint(Long id, int point) {
        Member member = getMemberByIdWithLock(id);
        member.sumPoint(point);
        return member.getPoint();
    }

    private Member getMemberByIdWithLock(Long id) {
        return memberRepository.findByIdWithLock(id)
            .orElseThrow(InvalidMemberException::new);
    }

    private Member getMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(InvalidMemberException::new);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(InvalidMemberException::new);
    }

    private void passwordCheck(String password, Member member) {
        if (!member.getPassword().equals(password)) {
            throw new PasswordNotMatchException();
        }
    }
}
