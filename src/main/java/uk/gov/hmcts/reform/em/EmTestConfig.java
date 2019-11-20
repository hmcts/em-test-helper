package uk.gov.hmcts.reform.em;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("uk.gov.hmcts.reform.em.test")
@EnableFeignClients({"uk.gov.hmcts.reform.idam.client", "uk.gov.hmcts.reform.em.test"})
public class EmTestConfig {


}
