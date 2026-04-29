package uk.gov.hmcts.reform.em.test.ccddefinition;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@ConditionalOnProperty("ccd-def.api.url")
public class CcdDefImportApi {

    private final String url;
    private final RestTemplate restTemplate;

    public CcdDefImportApi(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public String importCaseDefinition(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorisation,
                                       @RequestHeader("ServiceAuthorization") String serviceAuth,
                                       @RequestPart MultipartFile file) {

        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap();
        parameters.add("file", buildPartFromFile(file));

        HttpHeaders httpHeaders = setHttpHeaders(authorisation, serviceAuth);

        // TODO: Using the pre-Spring-7 constructor HttpEntity(T, MultiValueMap<String, String>) intentionally.
        // The Spring 7 constructor HttpEntity(@Nullable T, @Nullable HttpHeaders) does not exist in older
        // Spring versions. Casting to MultiValueMap<String, String> ensures runtime compatibility for
        // consumers of this library who may be on Spring 5/6.
        @SuppressWarnings("removal")
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(
                parameters, (MultiValueMap<String, String>) httpHeaders
        );

        String result = restTemplate.postForObject(url + "/import", httpEntity, String.class);
        return result;

    }

    private HttpHeaders setHttpHeaders(String authorizationToken, String serviceAuth) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authorizationToken);
        headers.add("ServiceAuthorization", serviceAuth);

        headers.set(HttpHeaders.CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE);

        return headers;
    }

    // TODO: Using the pre-Spring-7 constructor HttpEntity(T, MultiValueMap<String, String>) intentionally.
    // The Spring 7 constructor HttpEntity(@Nullable T, @Nullable HttpHeaders) does not exist in older
    // Spring versions. Casting to MultiValueMap<String, String> ensures runtime compatibility for
    // consumers of this library who may be on Spring 5/6.
    @SuppressWarnings("removal")
    private static HttpEntity<Resource> buildPartFromFile(MultipartFile file) {
        return new HttpEntity<>(buildByteArrayResource(file), (MultiValueMap<String, String>) buildPartHeaders(file));
    }

    private static HttpHeaders buildPartHeaders(MultipartFile file) {
        requireNonNull(file.getContentType());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getContentType()));
        return headers;
    }

    private static ByteArrayResource buildByteArrayResource(MultipartFile file) {
        try {
            return new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException ioException) {
            throw new IllegalStateException(ioException);
        }
    }

}
