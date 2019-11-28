package uk.gov.hmcts.reform.em.test.ccddefinition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(
        name = "idam-test-ccd-def-user-role",
        url = "${ccd-def.api.url}"
)
@ConditionalOnProperty("ccd-def.api.url")
public interface CcdDefUserRoleApi {

    @RequestMapping(value = "/api/user-role", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity createUserRole(CreateUserRoleBody body,
                                  @RequestHeader("Authorization") String userToken,
                                  @RequestHeader("ServiceAuthorization") String s2sToken);

    @Data
    @AllArgsConstructor
    class CreateUserRoleBody {
        private String role;
        @JsonProperty("security_classification")
        private String securityClassification;
    }

}
