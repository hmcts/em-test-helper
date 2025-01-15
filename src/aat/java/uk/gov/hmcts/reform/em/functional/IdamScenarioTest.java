package uk.gov.hmcts.reform.em.functional;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.em.EmTestConfig;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {EmTestConfig.class})
@PropertySource(value = "classpath:application.yml")
@ExtendWith(SpringExtension.class)
class IdamScenarioTest {

    @Autowired
    IdamHelper idamHelper;

    @Test
    void testCreationAndDeletion() {
        idamHelper.createUser("ab.com", Stream.of("caseworker").toList());
        assertThat(idamHelper.authenticateUser("ab.com")).isNotEmpty();
        assertThat(idamHelper.getUserId("ab.com")).isNotEmpty();
        idamHelper.deleteUser("ab.com");

        assertThrows(FeignException.BadRequest.class, () ->
            idamHelper.authenticateUser("ab.com")
        );
    }

}
