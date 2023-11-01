package stock.chart;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import stock.chart.stock.repository.RedisStockRepository;
import stock.chart.stock.repository.StockCashPriorityRepository;
import stock.chart.stock.jmetertest.repository.TestCashStockRepository;

@Slf4j
@Configuration
@EnableRedisRepositories(basePackages = "stock.chart", includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    RedisStockRepository.class,
    StockCashPriorityRepository.class,
    TestCashStockRepository.class}))
@RequiredArgsConstructor
public class RedisConfig {


    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

//    @Value("${spring.redis.cluster.nodes}")
//    private List<String> clusterNodes;

    //     lettuce
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(new RedisClusterConfiguration(clusterNodes));
//    }


//     Redis template
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer(getClass().getClassLoader()));
        template.setValueSerializer(new JdkSerializationRedisSerializer(getClass().getClassLoader()));
        template.afterPropertiesSet();
        return template;
    }

}
