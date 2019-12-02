package uk.gov.hmcts.reform.em.test.ccddata;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

@Configuration
@ConditionalOnProperty({"core_case_data.api.url", "ccd-def.api.url"})
@ConditionalOnBean(name={"ccdS2sHelper", "idamHelper"})
public class CcdDataConfiguration {

    @Bean
    CcdDataHelper ccdDataHelper(IdamHelper idamHelper,
                                @Qualifier("ccdS2sHelper") S2sHelper ccdS2sHelper,
                                CoreCaseDataApi coreCaseDataApi) {
        return new CcdDataHelper(idamHelper, ccdS2sHelper, coreCaseDataApi);
    }
}
