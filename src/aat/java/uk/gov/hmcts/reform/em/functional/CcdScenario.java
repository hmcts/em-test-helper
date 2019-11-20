package uk.gov.hmcts.reform.em.functional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.em.test.CcdDataHelper;
import uk.gov.hmcts.reform.em.test.CcdDefinitionHelper;
import uk.gov.hmcts.reform.em.test.IdamHelper;
import uk.gov.hmcts.reform.em.test.RestTemplateConfig;
import uk.gov.hmcts.reform.em.test.S2sHelper;
import uk.gov.hmcts.reform.em.test.api.CcdDefImportApi;
import uk.gov.hmcts.reform.em.test.api.DeleteUserApi;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.IdamTestApi;

import static org.assertj.core.api.Assertions.assertThat;

@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.idam.client",
        "uk.gov.hmcts.reform.em.test", "uk.gov.hmcts.reform.ccd.client"})
@SpringBootTest(classes = {IdamClient.class, IdamApi.class, IdamHelper.class, DeleteUserApi.class,
        IdamTestApi.class, CcdDefinitionHelper.class, CcdDataHelper.class, S2sHelper.class,
        CcdDefImportApi.class, RestTemplate.class, RestTemplateConfig.class})
@PropertySource(value = "classpath:application.yml")
@EnableAutoConfiguration
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

    }

}
