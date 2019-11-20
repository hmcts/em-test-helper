package uk.gov.hmcts.reform.em.test.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
    name = "idam-test-api-delete",
    url = "${idam.api.url}/testing-support"
)
public interface DeleteUserApi {

    @RequestMapping(method = RequestMethod.DELETE, value = "/accounts/{email}")
    ResponseEntity deleteUser(@PathVariable("email") String email);

}
