package stock.chart.stock.exception;

public class StockNotFoundException extends RuntimeException {
        public StockNotFoundException() {
            super("존재하지 않는 주식 코드입니다.");
        }
}
