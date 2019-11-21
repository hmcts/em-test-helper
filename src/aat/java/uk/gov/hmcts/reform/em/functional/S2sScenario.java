package uk.gov.hmcts.reform.em.functional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.em.EmTestConfig;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@RunWith(SpringRunner.class)
public class S2sScenario {

    @Autowired
    S2sHelper s2sHelper;

    @Test
    public void getCcdGwS2sToken() {
        assertThat(s2sHelper.getCcdGwS2sToken()).isNotEmpty();
    }

}
