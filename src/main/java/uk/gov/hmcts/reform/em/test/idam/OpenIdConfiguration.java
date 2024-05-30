package uk.gov.hmcts.reform.em.test.idam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenIdConfiguration {

    public final String clientId;
    public final  String clientSecret;
    public final  String redirectUri;
    public final  String grantType;
    public final  String scope;

    public OpenIdConfiguration(
            @Value("${idam.client.id:}") String clientId,
            @Value("${idam.client.secret:}") String clientSecret,
            @Value("${idam.client.redirect_uri:}") String redirectUri,
            @Value("${idam.client.scope:}") String scope,
            @Value("${idam.client.grant_type:}") String grantType
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.grantType = grantType;
        this.scope = scope;
    }
}
