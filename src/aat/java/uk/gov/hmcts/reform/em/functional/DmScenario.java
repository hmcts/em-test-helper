package uk.gov.hmcts.reform.em.functional;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.document.domain.Document;
import uk.gov.hmcts.reform.em.test.*;
import uk.gov.hmcts.reform.em.test.api.CcdDefImportApi;
import uk.gov.hmcts.reform.em.test.api.DeleteUserApi;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.IdamTestApi;

import java.io.InputStream;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@EnableFeignClients(basePackages = {
        "uk.gov.hmcts.reform.idam.client",
        "uk.gov.hmcts.reform.em.test",
        "uk.gov.hmcts.reform.ccd.client"})
@SpringBootTest(classes = {IdamClient.class, IdamApi.class, IdamHelper.class, DeleteUserApi.class,
        IdamTestApi.class, CcdDefinitionHelper.class, CcdDataHelper.class, S2sHelper.class,
        RestTemplate.class, DmHelper.class, RestTemplateConfig.class, CcdDefImportApi.class})
@PropertySource(value = "classpath:application.yml")
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
public class DmScenario {

    @Autowired
    DmHelper dmHelper;

    @Test
    public void uploadAndGetId() throws Exception {
        String id =
                dmHelper.uploadAndGetId(
                        ClassLoader.getSystemClassLoader().getResourceAsStream("ccd_case_example.xlsx"),
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "ccd_case_example.xlsx");

        assertThat(id).isNotEmpty();

        Document document = dmHelper.getDocumentMetadata(id);

        assertThat(document).isNotNull();
        assertThat(document.originalDocumentName).isEqualTo("ccd_case_example.xlsx");

        InputStream documentBinaryStream = dmHelper.getDocumentBinary(id);
        assertThat(Arrays.equals(
            IOUtils.toByteArray(documentBinaryStream),
            IOUtils.toByteArray(ClassLoader.getSystemClassLoader().getResourceAsStream("ccd_case_example.xlsx"))))
                .isTrue();


    }
}
