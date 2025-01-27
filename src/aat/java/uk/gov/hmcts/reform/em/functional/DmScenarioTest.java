package uk.gov.hmcts.reform.em.functional;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.document.domain.Document;
import uk.gov.hmcts.reform.em.EmTestConfig;
import uk.gov.hmcts.reform.em.test.dm.DmHelper;

import java.io.InputStream;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@ExtendWith(SpringExtension.class)
class DmScenarioTest {

    @Autowired
    DmHelper dmHelper;

    @Test
    void uploadAndGetId() throws Exception {
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
