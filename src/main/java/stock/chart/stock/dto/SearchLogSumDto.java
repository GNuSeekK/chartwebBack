package stock.chart.stock.dto;

import lombok.Getter;

@Getter
public class SearchLogSumDto {

    private final Long sum;
    private final String code;

    public SearchLogSumDto(Long sum, String code) {
        this.sum = sum;
        this.code = code;
    }

    @Override
    public String toString() {
        return "SearchLogSumDto{" +
            "sum=" + sum +
            ", code='" + code + '\'' +
            '}';
    }
}
