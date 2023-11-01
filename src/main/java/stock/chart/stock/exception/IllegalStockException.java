package stock.chart.stock.exception;

public class IllegalStockException extends RuntimeException {

    public IllegalStockException(String message) {
        super(message);
    }
    public IllegalStockException() {
        super("종목 코드가 잘못되었습니다");
    }
}
