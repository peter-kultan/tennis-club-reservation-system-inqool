package cz;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }
}
