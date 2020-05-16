package uk.gov.hmcts.reform.em.test.ccddefinition;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConditionalOnProperty(prefix = "ccd-def", value = "api.url")
public class CcdDefImportApiConfiguration {

    @Bean
    CcdDefImportApi ccdDefImportApi(@Value("${ccd-def.api.url}") String url, RestTemplate restTemplate) {
        return new CcdDefImportApi(url, restTemplate);
    }

}
