package stock.chart;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import stock.chart.domain.redis.CashStock;
import stock.chart.domain.redis.CashStockPrice;
import stock.chart.stock.repository.RedisStockRepository;
import stock.chart.stock.repository.StockCashPriorityRepository;
import stock.chart.stock.repository.TestCashStockRepository;

@Slf4j
@Configuration
@EnableRedisRepositories(basePackages = "stock.chart", includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    RedisStockRepository.class,
    StockCashPriorityRepository.class,
    TestCashStockRepository.class}))
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;
    @Value("${spring.redis.host}")
    private String redisHost;


    // lettuce
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisProperties.getPort());
    }

    // Redis template
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

//    @Bean(name = "redisPriceTemplate")
//    public RedisTemplate<?, ?> redisPriceTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new JdkSerializationRedisSerializer(getClass().getClassLoader()));
//        template.setValueSerializer(new JdkSerializationRedisSerializer(getClass().getClassLoader()));
//        template.afterPropertiesSet();
//        return template;
//    }

}
