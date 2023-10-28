package stock.chart.domain.redis;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class RedisInteger implements Serializable, Comparable<RedisInteger> {
    private int value;

    public RedisInteger(Integer value) {
        this.value = value;
    }

    public void increase() {
        ++value;
    }

    public void decrease() {
        --value;
    }

    @Override
    public int compareTo(RedisInteger o) {
        return this.value - o.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RedisInteger) {
            return this.value == ((RedisInteger) o).value;
        }
        return false;
    }
}
