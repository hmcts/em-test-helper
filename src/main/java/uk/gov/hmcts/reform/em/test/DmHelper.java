package uk.gov.hmcts.reform.em.test;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.document.DocumentDownloadClientApi;
import uk.gov.hmcts.reform.document.DocumentMetadataDownloadClientApi;
import uk.gov.hmcts.reform.document.DocumentUploadClientApi;
import uk.gov.hmcts.reform.document.domain.Classification;
import uk.gov.hmcts.reform.document.domain.Document;
import uk.gov.hmcts.reform.document.domain.UploadResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DmHelper {

    private final DocumentUploadClientApi documentUploadClientApi;
    private final DocumentDownloadClientApi documentDownloadClientApi;
    private final DocumentMetadataDownloadClientApi documentMetadataDownloadClientApi;
    private final S2sHelper s2sHelper;

    public DmHelper(DocumentUploadClientApi documentUploadClientApi,
                    DocumentDownloadClientApi documentDownloadClientApi,
                    DocumentMetadataDownloadClientApi documentMetadataDownloadClientApi,
                    S2sHelper s2sHelper) {
        this.documentUploadClientApi = documentUploadClientApi;
        this.documentDownloadClientApi = documentDownloadClientApi;
        this.documentMetadataDownloadClientApi = documentMetadataDownloadClientApi;
        this.s2sHelper = s2sHelper;
    }

    public String uploadAndGetId(InputStream inputStream, String contentType, String fileName) throws IOException {

        final MultipartFile multipartFile = new MockMultipartFile(
                fileName,
                fileName,
                contentType,
                inputStream);

        UploadResponse uploadResponse = documentUploadClientApi.upload(
                null,
                s2sHelper.getCcdGwS2sToken(),
                null,
                new ArrayList<>(),
                Classification.PUBLIC,
                Stream.of(multipartFile).collect(Collectors.toList()));

        String href = uploadResponse.getEmbedded().getDocuments().get(0).links.self.href;

        return href.substring(uploadResponse.getEmbedded().getDocuments().get(0).links.self.href
                .lastIndexOf('/') + 1);

    }

    public Document getDocumentMetadata(String id) {
        return documentMetadataDownloadClientApi.getDocumentMetadata(
                null, s2sHelper.getCcdGwS2sToken(), "caseworker", null,
                String.format("documents/%s", id));
    }

    public InputStream getDocumentBinary(String id) throws IOException {
        ResponseEntity<Resource> responseEntity =
                documentDownloadClientApi.downloadBinary(null, s2sHelper.getCcdGwS2sToken(),
                        "caseworker", null, String.format("documents/%s/binary", id));
        return responseEntity.getBody().getInputStream();
    }


}
