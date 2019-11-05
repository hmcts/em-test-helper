package uk.gov.hmcts.reform.em.test;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class CcdDefinitionHelper {

    private final IdamHelper idamHelper;
    private final S2sHelper s2sHelper;
    private final CcdDefApi ccdDefApi;

    public CcdDefinitionHelper(IdamHelper idamHelper, S2sHelper s2sHelper, CcdDefApi ccdDefApi) {
        this.idamHelper = idamHelper;
        this.s2sHelper = s2sHelper;
        this.ccdDefApi = ccdDefApi;
    }

    public void importDefinitionFile(String username, String userRole, InputStream caseDefFile) throws IOException {

        ccdDefApi.createUserRole(idamHelper.getAuthenticatedUserIdamToken(username), s2sHelper.getCcdGwS2sToken(), userRole);

        MultipartFile multipartFile = new MockMultipartFile(
                "x",
                "x",
                "application/octet-stream",
                caseDefFile);

        ccdDefApi.importCaseDefinition(idamHelper.getAuthenticatedUserIdamToken(username), s2sHelper.getCcdGwS2sToken(), multipartFile);

    }


}
