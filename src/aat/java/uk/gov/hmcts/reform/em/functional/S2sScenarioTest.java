package uk.gov.hmcts.reform.em.functional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.em.EmTestConfig;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@ExtendWith(SpringExtension.class)
class S2sScenarioTest {

    @Autowired
    S2sHelper s2sHelper;

    @Autowired(required = false)
    @Qualifier("ccdS2sHelper")
    S2sHelper ccdS2sHelper;

    @Autowired(required = false)
    @Qualifier("xuiS2sHelper")
    S2sHelper xuiS2sHelper;

    @Test
    void getS2sToken() {
        assertThat(s2sHelper.getS2sToken()).isNotEmpty();
    }

    @Test
    void getCcdS2sToken() {
        assumeTrue(Objects.nonNull(ccdS2sHelper));
        assertThat(ccdS2sHelper.getS2sToken()).isNotEmpty();
    }

    @Test
    void getXuiS2sToken() {
        assumeTrue(Objects.nonNull(xuiS2sHelper));
        assertThat(xuiS2sHelper.getS2sToken()).isNotEmpty();
    }
}
