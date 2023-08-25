package online.k12code.spikeservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpikeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpikeServiceApplication.class, args);
    }

}
