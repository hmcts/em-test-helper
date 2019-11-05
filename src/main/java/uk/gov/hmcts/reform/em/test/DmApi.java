package uk.gov.hmcts.reform.em.test;

import com.fasterxml.jackson.databind.JsonNode;
import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FeignClient(
        name = "dm-api",
        url = "${dm.api.url}"
)
public interface DmApi {

    @RequestMapping(value = "/documents", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Headers({"ServiceAuthorization: {s2s_token}"})
    @ResponseBody
    JsonNode uploadFile(
            @Param("s2s_token") String s2sToken,
            @RequestPart(value = "classification") String classification,
            @RequestPart(value = "files") MultipartFile file) throws IOException;

}
