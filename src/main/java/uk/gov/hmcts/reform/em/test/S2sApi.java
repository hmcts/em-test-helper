package uk.gov.hmcts.reform.em.test;

import feign.Body;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
    name = "s2s-api",
    url = "${s2s.api.url}"
)
public interface S2sApi {

    @RequestMapping(method = RequestMethod.POST, value = "/lease")
    @Body("{\"microservice\": \"{microservice_name}\", \"oneTimePassword\": \"{otp}\"}")
    ResponseEntity generateToken(@Param("microservice_name") String microserviceName, @Param("otp") String otp);

}
