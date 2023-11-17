package online.k12code.limitServe;

import online.k12code.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableSwaggerDoc
@EnableAsync
@SpringBootApplication
public class AopRedisLimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopRedisLimitApplication.class, args);
    }

}
