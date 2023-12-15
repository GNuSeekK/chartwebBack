package stock.chart.news.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "image_url")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RedisImageCash {

    @Id
    @Indexed
    private String url;
    private String imageUrl;
}
