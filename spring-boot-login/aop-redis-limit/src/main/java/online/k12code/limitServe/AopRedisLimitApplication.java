package online.k12code.limitServe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AopRedisLimitApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopRedisLimitApplication.class, args);
    }

}
