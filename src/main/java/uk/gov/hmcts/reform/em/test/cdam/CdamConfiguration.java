package uk.gov.hmcts.reform.em.test.cdam;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.ccd.document.am.feign.CaseDocumentClientApi;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

@Configuration
@ConditionalOnProperty(prefix = "case_document_am", value = "url")
public class CdamConfiguration {

    @Bean
    public CdamHelper cdamHelper(CaseDocumentClientApi caseDocumentClientApi,
                                 @Qualifier("xuiS2sHelper") S2sHelper s2sHelper, IdamHelper idamHelper) {
        return new CdamHelper(caseDocumentClientApi, s2sHelper, idamHelper);
    }

}
