package stock.chart.stock.jmetertest.repository;

import java.util.Optional;
import org.springframework.data.redis.core.RedisTemplate;
import stock.chart.stock.jmetertest.entity.TestCashStock;

public class TestCashStockCustomRepositoryImpl implements
    TestCashStockCustomRepository {

    private final RedisTemplate<String, TestCashStock> redisTemplate;

    public TestCashStockCustomRepositoryImpl(RedisTemplate<String, TestCashStock> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<TestCashStock> getCashStockValue(String code) {
        TestCashStock testCashStock = redisTemplate.opsForValue().get(code);
        if (testCashStock != null) {
            return Optional.of(testCashStock);
        }
        return Optional.empty();
    }

    @Override
    public void setCashStockValue(String code, TestCashStock testCashStock) {
        redisTemplate.opsForValue().set(code, testCashStock);
    }
}
