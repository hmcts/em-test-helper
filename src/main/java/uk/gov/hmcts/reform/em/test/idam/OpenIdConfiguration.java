package uk.gov.hmcts.reform.em.test.idam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenIdConfiguration {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;
    private String scope;

    public OpenIdConfiguration(@Value("${idam.client.id:}") String clientId,
                               @Value("${idam.client.secret:}") String clientSecret,
                               @Value("${idam.client.redirect_uri:}") String redirectUri,
                               @Value("${idam.client.scope:}") String scope,
                               @Value("${idam.client.grantType:}") String grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
        this.scope = scope;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClient_secret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getScope() {
        return scope;
    }
}
