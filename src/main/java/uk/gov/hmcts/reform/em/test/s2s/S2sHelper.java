package uk.gov.hmcts.reform.em.test.s2s;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Value;

public class S2sHelper {

    private final String totpSecret;
    private final String microserviceName;
    private final S2sApi s2sApi;

    public S2sHelper(@Value("${s2s.api.secret}") String totpSecret,
                     @Value("${s2s.api.serviceName}") String microserviceName,
                     S2sApi s2sApi) {
        this.totpSecret = totpSecret;
        this.microserviceName = microserviceName;
        this.s2sApi = s2sApi;
    }

    public String getS2sToken() {
        return generateS2sToken(microserviceName, totpSecret);
    }

    public String generateS2sToken(String microserviceName, String microserviceSecret) {
        S2sApi.S2sPostBody s2sPostBody = new S2sApi.S2sPostBody(
                microserviceName,
                String.valueOf(new GoogleAuthenticator().getTotpPassword(microserviceSecret)));

        return s2sApi.generateToken(s2sPostBody);

    }

}
