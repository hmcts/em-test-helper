package uk.gov.hmcts.reform.em.test.idam.client.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Builder
@SuppressWarnings({"ParameterName","MemberName"})
public class OpenIdAuthUserRequest {

    private String grant_type;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String scope;
    private String username;
    private String password;

    public OpenIdAuthUserRequest(
            String grantType,
            String clientId,
            String clientSecret,
            String redirectUri,
            String scope,
            String username,
            String password
    ) {
        this.grant_type = grantType;
        this.client_id = clientId;
        this.client_secret = clientSecret;
        this.redirect_uri = redirectUri;
        this.scope = scope;
        this.username = username;
        this.password = password;
    }

    public String getGrantType() {
        return grant_type;
    }

    public String getClientId() {
        return client_id;
    }

    public String getRedirectUri() {
        return redirect_uri;
    }

    public String getScope() {
        return scope;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
