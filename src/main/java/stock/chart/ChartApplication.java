package stock.chart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing // test
@SpringBootApplication
@EnableAsync // 비동기 처리를 위한 어노테이션
@EnableJpaRepositories
public class ChartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChartApplication.class, args);
    }

}
