package uk.gov.hmcts.reform.em.functional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.em.EmTestConfig;
import uk.gov.hmcts.reform.em.test.ccddata.CcdDataHelper;
import uk.gov.hmcts.reform.em.test.ccddefinition.CcdDefinitionHelper;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@ExtendWith(SpringExtension.class)
class CcdScenarioTest {

    @Autowired
    CcdDefinitionHelper ccdDefinitionHelper;

    @Autowired
    CcdDataHelper ccdDataHelper;

    @Autowired
    IdamHelper idamHelper;

    @Test
    void testCaseCreationAndRetrieval() throws Exception {
        ccdDefinitionHelper.createCcdImportUser("bundle-tester@gmail.com","caseworker-publiclaw");

        ccdDefinitionHelper.importDefinitionFile("bundle-tester@gmail.com", "caseworker-publiclaw",
            ClassLoader.getSystemClassLoader().getResourceAsStream("ccd_case_example.xlsx"));

        CaseDetails caseDetails = ccdDataHelper.createCase("bundle-tester@gmail.com",
                "PUBLICLAW",
                "CCD_BUNDLE_MVP_ASYNC_TEST4",
                "createCase", null
                );

        assertThat(caseDetails).isNotNull();

        CaseDetails retrievedCaseDetails = ccdDataHelper.getCase("bundle-tester@gmail.com",
                caseDetails.getId().toString());

        assertThat(retrievedCaseDetails).isNotNull();

        ccdDataHelper.triggerEvent("bundle-tester@gmail.com", caseDetails.getId().toString(), "editCaseDetails");

    }

    @Test
    void testFailedCaseCreationDueToCorruptFile() throws IOException {
        ccdDefinitionHelper.createCcdImportUser("bundle-tester@gmail.com","caseworker-publiclaw");

        try (InputStream inputStream =
                ClassLoader.getSystemClassLoader().getResourceAsStream("corrupt_ccd_definition.xlsx")) {
            assertThrows(HttpClientErrorException.class, () ->
                ccdDefinitionHelper.importDefinitionFile("bundle-tester@gmail.com",
                    "caseworker-publiclaw",
                    inputStream)
            );
        }
    }

}
