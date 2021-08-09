package uk.gov.hmcts.reform.em.test.cdam;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.ccd.document.am.feign.CaseDocumentClientApi;
import uk.gov.hmcts.reform.ccd.document.am.model.Document;
import uk.gov.hmcts.reform.ccd.document.am.model.DocumentUploadRequest;
import uk.gov.hmcts.reform.ccd.document.am.model.UploadResponse;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@AllArgsConstructor
public class CdamHelper {

    private final CaseDocumentClientApi caseDocumentClientApi;
    private final S2sHelper s2sHelper;
    private final IdamHelper idamHelper;

    public UploadResponse uploadDocuments(String username,
                                          DocumentUploadRequest uploadRequest) {
        return caseDocumentClientApi.uploadDocuments(idamHelper.authenticateUser(username), "Bearer " +s2sHelper.getS2sToken(),
                                                    uploadRequest );
    }

    public Document getDocumentMetadata(String username, UUID documentId) {
        return caseDocumentClientApi.getMetadataForDocument(idamHelper.authenticateUser(username),
            s2sHelper.getS2sToken(), documentId);
    }

    public InputStream getDocumentBinary(String username, UUID documentId) throws IOException {
        ResponseEntity<Resource> responseEntity =
            caseDocumentClientApi.getDocumentBinary(idamHelper.authenticateUser(username),
                s2sHelper.getS2sToken(), documentId);
        return responseEntity.getBody().getInputStream();
    }

}
