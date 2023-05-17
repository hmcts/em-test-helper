package uk.gov.hmcts.reform.em.test.s2s;

import feign.codec.Decoder;
import feign.codec.StringDecoder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
    name = "s2s-api",
    url = "${s2s.api.url}",
    configuration = S2sApi.Config.class
)
@ConditionalOnProperty("s2s.api.url")
public interface S2sApi {

    @PostMapping(value = "/lease",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.TEXT_PLAIN_VALUE)
    String generateToken(S2sPostBody s2sPostBody);

    @Data
    @AllArgsConstructor
    class S2sPostBody {
        private String microservice;
        private String oneTimePassword;
    }

    class Config {
        @Bean
        Decoder stringDecoder() {
            return new StringDecoder();
        }
    }

}
