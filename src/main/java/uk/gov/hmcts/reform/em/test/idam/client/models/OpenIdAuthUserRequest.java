package uk.gov.hmcts.reform.em.test.idam.client.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Builder
public class OpenIdAuthUserRequest {

    private String grantType;

    private String clientId;

    private String clientSecret;

    private String redirectUri;
    private String scope;
    private String username;
    private String password;

    public OpenIdAuthUserRequest(String grantType, String clientId, String clientSecret,
                                 String redirectUri, String scope,
                                    String username, String password) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.username = username;
        this.password = password;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScope() {
        return scope;
    }

    public String getClient_secret() {
        return clientSecret;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
