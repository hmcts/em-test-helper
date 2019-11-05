package uk.gov.hmcts.reform.em.test;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class DmHelper {

    private final DmApi dmApi;
    private final S2sHelper s2sHelper;

    public DmHelper(DmApi dmApi, S2sHelper s2sHelper) {
        this.dmApi = dmApi;
        this.s2sHelper = s2sHelper;
    }

    public String uploadAndGetId(InputStream inputStream, String contentType) throws IOException {
        final MultipartFile multipartFile = new MockMultipartFile(
                "x",
                "x",
                contentType,
                inputStream);
        return dmApi.uploadFile(s2sHelper.getEmGwS2sToken(), "PUBLIC", multipartFile)
                .at("_embedded/documents/0/_links/self/href").asText();
    }



}
