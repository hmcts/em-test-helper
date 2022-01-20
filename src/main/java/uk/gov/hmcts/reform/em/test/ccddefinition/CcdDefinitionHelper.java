package uk.gov.hmcts.reform.em.test.ccddefinition;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CcdDefinitionHelper {

    private final IdamHelper idamHelper;
    private final S2sHelper s2sHelper;
    private final CcdDefImportApi ccdDefImportApi;
    private final CcdDefUserRoleApi ccdDefUserRoleApi;

    public CcdDefinitionHelper(IdamHelper idamHelper, S2sHelper s2sHelper,
                               CcdDefImportApi ccdDefImportApi, CcdDefUserRoleApi ccdDefUserRoleApi) {
        this.idamHelper = idamHelper;
        this.s2sHelper = s2sHelper;
        this.ccdDefImportApi = ccdDefImportApi;
        this.ccdDefUserRoleApi = ccdDefUserRoleApi;
    }

    public void createCcdImportUser(String username, String userRole) {
        this.idamHelper.createUser(username, Stream.of(userRole, "ccd-import").collect(Collectors.toList()));
    }

    public String importDefinitionFile(String username, String userRole, InputStream caseDefFile) throws IOException {

        ccdDefUserRoleApi.createUserRole(new CcdDefUserRoleApi.CreateUserRoleBody(userRole, "PUBLIC"),
                idamHelper.authenticateUser(username), s2sHelper.getS2sToken());

        MultipartFile multipartFile = new MockMultipartFile(
                "x",
                "x",
                "application/octet-stream",
                caseDefFile);

        String result = ccdDefImportApi.importCaseDefinition(idamHelper.authenticateUser(username),
            s2sHelper.getS2sToken(), multipartFile);

        return result;
    }



}
