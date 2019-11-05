package uk.gov.hmcts.reform.em.test;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FeignClient(
    name = "idam-test-ccd-def",
    url = "${ccd-def.api.url}/testing-support",
    configuration = {MultipartSupportConfig.class}
)
public interface CcdDefApi {

    @RequestMapping(method = RequestMethod.PUT, value = "/api/user-role")
    @Headers({"Authorization: {user_token}", "ServiceAuthorization: {s2s_token}"})
    @Body("{\"role\":\"{user_role}\",\"security_classification\":\"PUBLIC\"}")
    ResponseEntity createUserRole(@Param("user_token") String userToken,
                                  @Param("s2s_token") String s2sToken,
                                  @Param("user_role") String userRole);

    @RequestMapping(value = "/import", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Headers({"Authorization: {user_token}", "ServiceAuthorization: {s2s_token}"})
    @ResponseBody String importCaseDefinition(
            @Param("user_token") String userToken,
            @Param("s2s_token") String s2sToken,
            @RequestPart(value = "file") MultipartFile file) throws IOException;



}
