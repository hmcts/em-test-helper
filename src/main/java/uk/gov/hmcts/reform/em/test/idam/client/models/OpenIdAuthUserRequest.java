package uk.gov.hmcts.reform.em.test.idam.client.models;

public class OpenIdAuthUserRequest {

    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String client_secret;
    private String scope;

    public OpenIdAuthUserRequest(String grant_type, String client_id, String redirect_uri,
                                 String client_secret, String scope) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.redirect_uri = redirect_uri;
        this.client_secret = client_secret;
        this.scope = scope;
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

    public String getClientSecret() {
        return client_secret;
    }

    public String getScope() {
        return scope;
    }
}
