package uk.gov.hmcts.reform.em.functional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.em.EmTestConfig;
import uk.gov.hmcts.reform.em.test.ccddata.CcdDataHelper;
import uk.gov.hmcts.reform.em.test.ccddefinition.CcdDefinitionHelper;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@RunWith(SpringRunner.class)
public class CcdScenario {

    @Autowired
    CcdDefinitionHelper ccdDefinitionHelper;

    @Autowired
    CcdDataHelper ccdDataHelper;

    @Autowired
    IdamHelper idamHelper;

    @Test
    public void testCaseCreationAndRetrieval() throws Exception {
        ccdDefinitionHelper.createCcdImportUser("bundle-tester@gmail.com","caseworker-publiclaw");

        ccdDefinitionHelper.importDefinitionFile("bundle-tester@gmail.com","caseworker-publiclaw",
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

}
