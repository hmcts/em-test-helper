package uk.gov.hmcts.reform.em.test.dm;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.document.DocumentDownloadClientApi;
import uk.gov.hmcts.reform.document.DocumentMetadataDownloadClientApi;
import uk.gov.hmcts.reform.document.DocumentUploadClientApi;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

@Configuration
@ConditionalOnProperty({"document_management.url", "s2s.api.url"})
public class DmConfiguration {

    @Bean
    public DmHelper dmHelper(DocumentUploadClientApi documentUploadClientApi,
                             DocumentDownloadClientApi documentDownloadClientApi,
                             DocumentMetadataDownloadClientApi documentMetadataDownloadClientApi,
                             S2sHelper s2sHelper) {
        return new DmHelper(documentUploadClientApi,
                documentDownloadClientApi,
                documentMetadataDownloadClientApi,
                s2sHelper);
    }

}
