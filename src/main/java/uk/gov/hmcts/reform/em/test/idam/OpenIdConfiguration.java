package uk.gov.hmcts.reform.em.test.idam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenIdConfiguration {

    private String clientId;
    private String redirectUri;
    private String clientSecret;
    private String grant_type;
    private String scope;

    public OpenIdConfiguration(@Value("${idam.client.id:}") String clientId,
                               @Value("${idam.client.redirect_uri:}") String redirectUri,
                               @Value("${idam.client.secret:}") String clientSecret,
                               @Value("${idam.client.scope:}") String scope,
                               @Value("${idam.client.grant_type:}") String grant_type) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.grant_type = grant_type;
        this.scope = scope;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getGrantType() {
        return grant_type;
    }

    public String getScope() {
        return scope;
    }
}
