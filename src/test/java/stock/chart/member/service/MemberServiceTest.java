package stock.chart.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
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

    @BeforeEach
    void setUp() {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
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
    void 포인트낙관적락테스트() throws Exception {
        // given
        Member member = saveTestMember();
        int threadCount = 5;
        int point = 1000;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        // when
        Member newMember = memberRepository.findByEmail(member.getEmail()).get();
        Long memberId = newMember.getId();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            try {
                executorService.execute(() -> {
                    try {
                        memberService.changePoint(memberId, point);
                        successCount.incrementAndGet();
                    } catch (ObjectOptimisticLockingFailureException e) {
                        failCount.incrementAndGet();
                    }
                });
            } finally {
                latch.countDown();
            }
        }
        latch.await();

        Thread.sleep(1000);
        // then
        Member findMember = memberRepository.findByEmail(member.getEmail()).get();
        assertThat(findMember.getPoint()).isEqualTo(point * successCount.intValue());
        assertThat(findMember.getVersion()).isEqualTo(successCount.intValue());
        assertThat(successCount.intValue() + failCount.intValue()).isEqualTo(threadCount);
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
            try {
                executorService.execute(() -> {
                    memberService.changePoint(memberId, point);
                    count.addAndGet(point);
                    }
                );
            } finally {
                latch.countDown();
            }
        }
        latch.await();
        // then

        Thread.sleep(1000);
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