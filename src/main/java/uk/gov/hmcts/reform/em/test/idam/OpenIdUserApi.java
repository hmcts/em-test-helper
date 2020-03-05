package uk.gov.hmcts.reform.em.test.idam;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.em.test.idam.client.models.OpenIdAuthUserRequest;
import uk.gov.hmcts.reform.em.test.idam.client.models.OpenIdAuthUserResponse;
import uk.gov.hmcts.reform.idam.client.CoreFeignConfiguration;

@FeignClient(
        name = "open-id-user-api",
        url = "${idam.api.url}",
        configuration = {CoreFeignConfiguration.class}
        )
public interface OpenIdUserApi {

    @PostMapping(
            value = {"/o/token"},
            consumes = {"application/x-www-form-urlencoded"}
    )
    OpenIdAuthUserResponse authenticateUser(@RequestHeader("Authorization") String authorisation,
                                            @RequestBody OpenIdAuthUserRequest openIdAuthUserRequest);

}
