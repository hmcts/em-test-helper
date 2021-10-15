package uk.gov.hmcts.reform.em.test.ccddata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

@Configuration
@ConditionalOnProperty({"core_case_data.api.url", "ccd-def.api.url"})
@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.ccd.client"})
public class CcdDataConfiguration {

    @Bean
    CcdDataHelper ccdDataHelper(IdamHelper idamHelper,
                                @Qualifier("xuiS2sHelper") @Autowired(required = false) S2sHelper xuiS2sHelper,
                                CoreCaseDataApi coreCaseDataApi) {
        return new CcdDataHelper(idamHelper, xuiS2sHelper, coreCaseDataApi);
    }
}
