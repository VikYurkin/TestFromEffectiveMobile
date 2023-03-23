package ru.VYurkin.TestFromEffectiveMobile;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class TestFromEffectiveMobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestFromEffectiveMobileApplication.class, args);
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
