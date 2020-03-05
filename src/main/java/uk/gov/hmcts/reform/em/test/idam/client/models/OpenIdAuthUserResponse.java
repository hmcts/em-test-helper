package uk.gov.hmcts.reform.em.test.idam.client.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class OpenIdAuthUserResponse {

    private String accessToken;
    private String refreshToken;
    private String scope;
    private String idToken;
    private String tokenType;
    private String expiresIn;

    @JsonCreator
    public OpenIdAuthUserResponse(@JsonProperty("access_token") String accessToken,
                                  @JsonProperty("refresh_token") String refreshToken,
                                  @JsonProperty("scope") String scope,
                                  @JsonProperty("id_token") String idToken,
                                  @JsonProperty("token_type") String tokenType,
                                  @JsonProperty("expires_in") String expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.scope = scope;
        this.idToken = idToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getExpiresIn() {
        return expiresIn;
    }
}
