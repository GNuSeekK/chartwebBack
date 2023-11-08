package stock.chart.member.service;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import stock.chart.domain.Member;
import stock.chart.member.repository.MemberRepository;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    public final MemberService memberService;
    public final MemberRepository memberRepository;

    @Autowired
    public MemberServiceTest(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @Test
    @Transactional
    void 포인트테스트() throws Exception {
        // given
        Member member = saveTestMember();

        // when
        memberService.changePoint(member.getId(), 1000);

        // then
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember.getPoint()).isEqualTo(1000);
    }

    @Test
    void 포인트동시성테스트() throws Exception {
        // given
        Member member = saveTestMember();
        int threadCount = 5;
        int point = 1000;
        AtomicInteger count = new AtomicInteger(0);
        // when

        Member newMember = memberRepository.findByEmail(member.getEmail()).get();
        Long memberId = newMember.getId();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
            memberService.changePoint(memberId, point);
            count.addAndGet(point);
            latch.countDown();
            });
        }
        latch.await();
        // then

        Member findMember = memberRepository.findByEmail(member.getEmail()).get();
        memberRepository.delete(findMember);
        assertThat(count.intValue()).isEqualTo(point * threadCount);
        assertThat(findMember.getPoint()).isEqualTo(point * threadCount);
    }

    private Member saveTestMember() {
        Member member = Member.builder()
            .email("test@test.com")
            .password("1234")
            .nickname("test")
            .build();
        memberRepository.saveAndFlush(member);
        return member;
    }
}