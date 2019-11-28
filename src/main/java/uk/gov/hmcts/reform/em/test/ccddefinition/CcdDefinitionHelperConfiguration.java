package uk.gov.hmcts.reform.em.test.ccddefinition;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

@Configuration
@ConditionalOnProperty({"ccd-def.api.url", "s2s.api.url", "idam.api.url"})
public class CcdDefinitionHelperConfiguration {

    @Bean
    CcdDefinitionHelper ccdDefinitionHelper(IdamHelper idamHelper, @Qualifier("ccdS2sHelper") S2sHelper ccdS2sHelper,
                                        CcdDefImportApi ccdDefImportApi, CcdDefUserRoleApi ccdDefUserRoleApi) {
        return new CcdDefinitionHelper(idamHelper, ccdS2sHelper, ccdDefImportApi, ccdDefUserRoleApi);
    }

}
