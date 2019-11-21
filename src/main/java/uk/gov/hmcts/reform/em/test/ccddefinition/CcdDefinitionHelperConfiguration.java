package uk.gov.hmcts.reform.em.test.ccddefinition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

@Configuration
@ConditionalOnProperty(prefix = "ccd-def", value = "api.url")
public class CcdDefinitionHelperConfiguration {

    @Bean
    CcdDefinitionHelper ccdDefinitionHelper(IdamHelper idamHelper, S2sHelper s2sHelper,
                                        CcdDefImportApi ccdDefImportApi, CcdDefUserRoleApi ccdDefUserRoleApi) {
        return new CcdDefinitionHelper(idamHelper, s2sHelper, ccdDefImportApi, ccdDefUserRoleApi);
    }

}
