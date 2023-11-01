package stock.chart.stock.exception;

public class RedisStockException extends RuntimeException {

    public RedisStockException(String message) {
        super(message);
    }

    public RedisStockException() {
        super("Redis에서 주식을 찾을 수 없습니다");
    }
}
