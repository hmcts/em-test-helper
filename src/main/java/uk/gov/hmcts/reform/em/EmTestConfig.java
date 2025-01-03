package uk.gov.hmcts.reform.em;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan({"uk.gov.hmcts.reform.em.test.**",
    "uk.gov.hmcts.reform.document",
    "uk.gov.hmcts.reform.ccd.document.am.config"})
@EnableAutoConfiguration
public class EmTestConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
