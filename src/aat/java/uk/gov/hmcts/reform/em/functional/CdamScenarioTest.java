package uk.gov.hmcts.reform.em.functional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.ccd.document.am.model.Document;
import uk.gov.hmcts.reform.ccd.document.am.model.DocumentUploadRequest;
import uk.gov.hmcts.reform.ccd.document.am.model.UploadResponse;
import uk.gov.hmcts.reform.document.domain.Classification;
import uk.gov.hmcts.reform.em.EmTestConfig;
import uk.gov.hmcts.reform.em.test.cdam.CdamHelper;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@ExtendWith(SpringExtension.class)
class CdamScenarioTest {

    @Autowired
    IdamHelper idamHelper;

    @Autowired
    CdamHelper cdamHelper;

    private static final String CASE_TYPE_ID = "BEFTA_CASETYPE_2_1";
    private static final String JURISDICTION_ID = "BEFTA_JURISDICTION_2";
    private static final String USERNAME = "ab@mail.com";

    @Test
    void testUploadDocuments() throws IOException {

        UploadResponse uploadResponse = getUploadDocumentResponse();

        assertThat(uploadResponse.getDocuments()).isNotEmpty();
        assertThat(uploadResponse.getDocuments().get(0).hashToken).isNotBlank();
        assertThat(uploadResponse.getDocuments().get(0).links).isNotNull();
        assertThat(uploadResponse.getDocuments().get(0).originalDocumentName).isNotBlank();
    }

    @Test
    void testGetDocumentMetadata() throws IOException {
        UploadResponse uploadResponse = getUploadDocumentResponse();
        UUID uuid = extractDocumentId(uploadResponse.getDocuments().get(0).links.self.href);
        Document document = cdamHelper.getDocumentMetadata(USERNAME, uuid);
        assertThat(document.metadata).isNotEmpty();
    }

    private UUID extractDocumentId(String href) {
        return UUID.fromString(href.substring(href.lastIndexOf('/') + 1));
    }

    private UploadResponse getUploadDocumentResponse() throws IOException {
        idamHelper.createUser(USERNAME, Stream.of("caseworker").toList());
        final MultipartFile multipartFile = new MockMultipartFile(
            "ccd_case_example.xlsx",
            "ccd_case_example.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            ClassLoader.getSystemClassLoader().getResourceAsStream("ccd_case_example.xlsx"));

        DocumentUploadRequest documentUploadRequest = new DocumentUploadRequest(Classification.PUBLIC.toString(),
            CASE_TYPE_ID, JURISDICTION_ID, Stream.of(multipartFile).toList());

        return cdamHelper.uploadDocuments(USERNAME, documentUploadRequest);
    }
}
