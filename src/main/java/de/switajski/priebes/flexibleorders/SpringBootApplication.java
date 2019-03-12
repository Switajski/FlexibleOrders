package de.switajski.priebes.flexibleorders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
@ComponentScan
public class SpringBootApplication {
  
  public static void main(String[] args) {
    
    SpringApplication.run(SpringBootApplication.class, args);
  }
}
