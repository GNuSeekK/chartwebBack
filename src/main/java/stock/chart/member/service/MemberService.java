package stock.chart.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.member.dto.MemberInfoDto;
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
}
