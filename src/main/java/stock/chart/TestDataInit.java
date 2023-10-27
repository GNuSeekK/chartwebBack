package stock.chart;

import java.time.LocalDate;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import stock.chart.domain.Member;
import stock.chart.domain.Stock;
import stock.chart.domain.StockDateId;
import stock.chart.domain.StockPrice;
import stock.chart.member.repository.MemberRepository;
import stock.chart.stock.repository.RedisStockRepository;
import stock.chart.stock.repository.StockPriceRepository;
import stock.chart.stock.repository.StockRepository;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;
    private final StockPriceRepository stockPriceRepository;
    private final RedisStockRepository redisStockRepository;
    private final EntityManager em;

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
//
//        Stock samsung = new Stock("005930", "삼성전자");
//        Stock sk하이닉스 = new Stock("000660", "SK하이닉스");
//
//        StockPrice samsungPrice1 = new StockPrice(new StockDateId("005930", LocalDate.of(2021, 1, 1)), 10000, 10000,
//            10000, 10000, 10000L);
//        StockPrice samsungPrice2 = new StockPrice(new StockDateId("005930", LocalDate.of(2021, 1, 2)), 20000, 20000,
//            20000, 20000, 20000L);
//        StockPrice samsungPrice3 = new StockPrice(new StockDateId("005930", LocalDate.of(2021, 1, 3)), 30000, 30000,
//            30000, 30000, 30000L);
//
//        StockPrice sk하이닉스Price1 = new StockPrice(new StockDateId("000660", LocalDate.of(2021, 1, 1)), 1000, 1000, 1000,
//            1000, 1000L);
//        StockPrice sk하이닉스Price2 = new StockPrice(new StockDateId("000660", LocalDate.of(2021, 1, 2)), 2000, 2000, 2000,
//            2000, 2000L);
//        StockPrice sk하이닉스Price3 = new StockPrice(new StockDateId("000660", LocalDate.of(2021, 1, 3)), 3000, 3000, 3000,
//            3000, 3000L);
//
//        samsung.addStockPrice(samsungPrice1);
//        samsung.addStockPrice(samsungPrice2);
//        samsung.addStockPrice(samsungPrice3);
//        sk하이닉스.addStockPrice(sk하이닉스Price1);
//        sk하이닉스.addStockPrice(sk하이닉스Price2);
//        sk하이닉스.addStockPrice(sk하이닉스Price3);
//
//        stockRepository.save(samsung);
//        stockRepository.save(sk하이닉스);


//        stockPriceRepository.save(samsungPrice1);
//        stockPriceRepository.save(samsungPrice2);
//        stockPriceRepository.save(samsungPrice3);
//        stockPriceRepository.save(sk하이닉스Price1);
//        stockPriceRepository.save(sk하이닉스Price2);
//        stockPriceRepository.save(sk하이닉스Price3);

//        redisStockRepository.save(samsung.toCashStock());
//        redisStockRepository.save(sk하이닉스.toCashStock());

        ///////asdfasdfsadf

    }

}