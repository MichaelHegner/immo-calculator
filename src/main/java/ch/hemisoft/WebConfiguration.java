package ch.hemisoft;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;

@Configuration
public class WebConfiguration {
    
    @Bean
    public RestOperations restTemplate(RestTemplateBuilder builder) {
       // Do any additional configuration here
       return builder.build();
    }
}
