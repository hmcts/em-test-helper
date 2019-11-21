package uk.gov.hmcts.reform.em.test.ccddata;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

@Configuration
@ConditionalOnProperty(prefix = "core_case_data", value = "api.url")
public class CcdDataConfiguration {

    @Bean
    CcdDataHelper ccdDataHelper(IdamHelper idamHelper, S2sHelper s2sHelper, CoreCaseDataApi coreCaseDataApi) {
        return new CcdDataHelper(idamHelper, s2sHelper, coreCaseDataApi);
    }
}
