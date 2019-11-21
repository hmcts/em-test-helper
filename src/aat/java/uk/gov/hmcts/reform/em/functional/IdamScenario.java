package uk.gov.hmcts.reform.em.functional;

import feign.FeignException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.em.EmTestConfig;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@RunWith(SpringRunner.class)
public class IdamScenario {

    @Autowired
    IdamHelper idamHelper;

    @Test(expected = FeignException.Unauthorized.class)
    public void testCreationAndDeletion() {
        idamHelper.createUser("a@b.com", Stream.of("caseworker").collect(Collectors.toList()));
        assertThat(idamHelper.authenticateUser("a@b.com")).isNotEmpty();
        assertThat(idamHelper.getUserId("a@b.com")).isNotEmpty();
        idamHelper.deleteUser("a@b.com");
        idamHelper.authenticateUser("a@b.com");
    }

}
