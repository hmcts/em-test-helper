package uk.gov.hmcts.reform.em.test.s2s;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty({"s2s.api.ccdGwSecret","s2s.api.ccdGwServiceName"})
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.em.test.s2s"})
public class CcdS2sConfiguration {

    @Bean
    public S2sHelper ccdS2sHelper(
            @Value("${s2s.api.ccdGwSecret}") String totpSecret,
            @Value("${s2s.api.ccdGwServiceName}") String microserviceName,
            S2sApi s2sApi) {
        return new S2sHelper(totpSecret,
                microserviceName,
                s2sApi);
    }

}
