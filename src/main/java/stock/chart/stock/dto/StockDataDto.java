package stock.chart.stock.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import stock.chart.domain.Board;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
public class StockDataDto {

    private String code;
    private String name;

    public StockDataDto(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
