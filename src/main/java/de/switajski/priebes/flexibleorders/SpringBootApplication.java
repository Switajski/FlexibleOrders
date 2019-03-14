package de.switajski.priebes.flexibleorders;

import de.switajski.priebes.flexibleorders.web.PdfView;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableAutoConfiguration
@Configuration
@ComponentScan
public class SpringBootApplication {
  
  @Bean
  public ViewResolver getViewResolver() {
    
    return (viewName, locale) -> viewName.equals(PdfView.class.getSimpleName()) ? new PdfView() : null;
  }
  
  public static void main(String[] args) {
    
    SpringApplication.run(SpringBootApplication.class, args);
  }
}
