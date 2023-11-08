package stock.chart.domain;

import com.sun.istack.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import stock.chart.domain.base.BaseTimeEntity;
import stock.chart.domain.redis.CashStock;
import stock.chart.domain.redis.CashStock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class Stock extends BaseTimeEntity implements Persistable<String> {

    @Id
    @Column(name = "stock_code")
    private String code;


    @NotNull
    private String name;

    public Stock(String code, String name) {
        this.code = code;
        this.name = name;
    }


    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StockPrice> stockPrices = new ArrayList<>();

    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY)
    private List<Board> board = new ArrayList<>();

    @Override
    public String getId() {
        return this.getCode();
    }

    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }

    /**
     * 양방향 연관관계 어시스트 메서드
     */
    public void setBoard(Board board) {
        this.board.add(board);
    }

    public void addStockPrice(StockPrice stockPrice) {
        this.stockPrices.add(stockPrice);
        stockPrice.setStock(this);
    }

    public CashStock toCashStock() {
        return CashStock.builder()
            .code(this.code)
            .cashStockPricesSet(
                this.stockPrices.stream()
                    .map(StockPrice::toCashStockPrice)
                    .collect(Collectors.toSet()))
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        Stock stock = (Stock) o;
        return Objects.equals(getCode(), stock.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
