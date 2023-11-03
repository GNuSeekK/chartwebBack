package stock.chart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import stock.chart.domain.Member;
import stock.chart.member.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final MemberRepository memberRepository;
    /**
     * 확인용 초기 데이터 추가
     */
    //test test test
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {

//        Member member = new Member("ssafy@ssafy.com", "ssafy", "싸피");
//        Member member2 = new Member("member@ssafy.com", "member", "멤버");
//        Member member3 = new Member("a237246@yahoo.co.jp", "dlrltjd", "이기성");
//
//        memberRepository.save(member);
//        memberRepository.save(member2);
//        memberRepository.save(member3);

        ///////asdfasdfsadf

    }

}