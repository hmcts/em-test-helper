package uk.gov.hmcts.reform.em.test.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.gov.hmcts.reform.idam.client.CoreFeignConfiguration;

@FeignClient(
    name = "s2s-api",
    url = "${s2s.api.url}",
    configuration = CoreFeignConfiguration.class
)
public interface S2sApi {

    @RequestMapping(method = RequestMethod.POST, value = "/lease",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    String generateToken(S2sPostBody s2sPostBody);

    @Data
    @AllArgsConstructor
    class S2sPostBody {
        private String microservice;
        private String oneTimePassword;
    }

}
