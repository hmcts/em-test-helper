package uk.gov.hmcts.reform.em.test.s2s;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "s2s", value = "api.url")
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.em.test.s2s"})
public class S2sConfiguration {

    @Bean
    public S2sHelper s2sHelper(
            @Value("${s2s.api.secret}") String ccdGwTotpSecret,
            @Value("${s2s.api.serviceName}") String ccdGwMicroserviceName,
            S2sApi s2sApi) {
        return new S2sHelper(ccdGwTotpSecret,
                ccdGwMicroserviceName,
                s2sApi);
    }

}
