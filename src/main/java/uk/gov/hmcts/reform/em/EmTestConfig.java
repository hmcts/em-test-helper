package uk.gov.hmcts.reform.em;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("uk.gov.hmcts.reform.**")
@EnableAutoConfiguration
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.**"})
public class EmTestConfig {


}
