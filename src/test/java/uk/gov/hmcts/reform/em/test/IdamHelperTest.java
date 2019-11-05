package uk.gov.hmcts.reform.em.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.idam.client.IdamApi;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.IdamTestApi;
import uk.gov.hmcts.reform.idam.client.models.GeneratePinResponse;
import uk.gov.hmcts.reform.idam.client.models.UserDetails;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.idam.client", "uk.gov.hmcts.reform.em.test"})
@SpringBootTest(classes = {IdamClient.class, IdamApi.class, IdamHelper.class, DeleteUserApi.class, IdamTestApi.class})
@PropertySource(value = "classpath:application.yml")
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
public class IdamHelperTest {
    private final String BEARER = "Bearer ";
    private final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ1c2FubmJyaGV2OWI0dGxzMzhy"
            + "MTI4dGdycCIsInN1YiI6IjI0IiwiaWF0IjoxNTUwNjk1Nzc5LCJleHAiOjE1NTA3MjQ1NzksImRhdGEiOiJjYXNld29ya2"
            + "VyLXNzY3MsY2FzZXdvcmtlci1zc2NzLWxvYTAiLCJ0eXBlIjoiQUNDRVNTIiwiaWQiOiIyNCIsImZvcmVuYW1lIjoiQnVs"
            + "ayBTY2FuIiwic3VybmFtZSI6IlN5c3RlbSBVcGRhdGUiLCJkZWZhdWx0LXNlcnZpY2UiOiJCU1AiLCJsb2EiOjAsImRlZm"
            + "F1bHQtdXJsIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6OTAwMC9wb2MvYnNwIiwiZ3JvdXAiOiJic3Atc3lzdGVtdXBkYXRlIn0.P"
            + "djD2Kjz6myH1p44CRCVztkl2lqkg0LXqiyoH7Hs2bg";
    private final String EXCHANGE_CODE_RESULT = String.format(
            "{\"access_token\":\"%s\",\"token_type\":\"Bearer\",\"expires_in\":28800}", TOKEN);
    private final String USER_LOGIN = "user@example.com";
    private final String USER_PASSWORD = "Password12";
    private final String EXCHANGE_CODE = "eEdhNnasWy7eNFAV";
    private final String PIN_AUTH_CODE = "abcdefgh123456789";
    private final String PIN = "ABCD1234";
    private String PIN_REDIRECT_URL;
    @Value("${idam.client.redirect_uri:}") private String REDIRECT_URI;
    @Value("${idam.client.id:}") private String CLIENT_ID;
    @Value("${idam.client.secret:}") private String CLIENT_SECRET;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IdamHelper idamHelper;

    @Rule
    public WireMockClassRule idamApiServer = new WireMockClassRule(WireMockSpring
            .options()
            .port(4501)
            .extensions(new ConnectionCloseExtension()));

    @Before
    public void setup() {
        PIN_REDIRECT_URL = REDIRECT_URI + "?code=" + PIN_AUTH_CODE;

        // See https://www.baeldung.com/jackson-optional for why this is needed
        objectMapper.registerModule(new Jdk8Module());
    }

    @Test
    public void test() {
        stubForAuthenticateUser(HttpStatus.OK);
        stubForToken();
        stubForDelete(USER_LOGIN);
        stubForUserCreation();
        final String bearerToken = idamHelper.authenticateUser(USER_LOGIN, Stream.of("caseworker").collect(Collectors.toList()));
        assertThat(bearerToken).isEqualTo(BEARER + TOKEN);
    }

    private void stubForAuthenticateUser(HttpStatus responseStatus) {
        final String OAUTH2_AUTHORIZE_ENDPOINT = "/oauth2/authorize";
        final String AUTH_TOKEN = "Basic dXNlckBleGFtcGxlLmNvbTpQYXNzd29yZDEy";
        final String SUCCESS_OAUTH_BODY = "{\"code\":\"eEdhNnasWy7eNFAV\"}";
        idamApiServer.stubFor(WireMock.post(OAUTH2_AUTHORIZE_ENDPOINT)
                .willReturn(aResponse()
                        .withStatus(responseStatus.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                        .withBody(SUCCESS_OAUTH_BODY)
                )
        );
    }

    private void stubForToken() {
        final String OAUTH2_TOKEN_ENDPOINT = "/oauth2/token";
        idamApiServer.stubFor(WireMock.post(OAUTH2_TOKEN_ENDPOINT)
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                        .withBody(EXCHANGE_CODE_RESULT)
                )
        );
    }

    private void stubForUserCreation() {
        idamApiServer.stubFor(WireMock.post("/testing-support/accounts")
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                )
        );
    }

    private void stubForDelete(String username) {
        final String DETAILS_ENDPOINT = "/testing-support/accounts/" + username;
        idamApiServer
                .stubFor(WireMock.delete(DETAILS_ENDPOINT).willReturn(aResponse().withStatus(HttpStatus.OK.value())));
    }

    private void stubForDetails(UserDetails userDetails) throws JsonProcessingException {
        final String DETAILS_ENDPOINT = "/details";
        idamApiServer.stubFor(WireMock.get(DETAILS_ENDPOINT)
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                        .withBody(objectMapper.writeValueAsString(userDetails))
                )
        );
    }

    private void stubForGeneratePin() throws JsonProcessingException {
        final String PIN_ENDPOINT = "/pin";
        GeneratePinResponse pinResponse = GeneratePinResponse.builder().pin(PIN).build();
        idamApiServer.stubFor(WireMock.post(PIN_ENDPOINT)
                .withHeader(HttpHeaders.AUTHORIZATION, new EqualToPattern(TOKEN))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE)
                        .withBody(objectMapper.writeValueAsString(pinResponse))
                )
        );
    }

    private void stubForAuthenticatePin() throws UnsupportedEncodingException {
        final String redirectUri = URLEncoder.encode(REDIRECT_URI, StandardCharsets.UTF_8.toString());
        final String PIN_ENDPOINT = String.format("/pin?client_id=%s&redirect_uri=%s&state", CLIENT_ID, redirectUri);
        idamApiServer.stubFor(WireMock.get(PIN_ENDPOINT)
                .willReturn(aResponse()
                        .withStatus(HttpStatus.FOUND.value())
                        .withHeader(HttpHeaders.LOCATION, PIN_REDIRECT_URL)
                )
        );
    }
}
