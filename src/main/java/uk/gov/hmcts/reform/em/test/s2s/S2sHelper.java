package uk.gov.hmcts.reform.em.test.s2s;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.beans.factory.annotation.Value;

public class S2sHelper {

    private final String ccdGwTotpSecret;
    private final String ccdGwMicroserviceName;
    private final S2sApi s2sApi;

    public S2sHelper(@Value("${s2s.api.ccdGwSecret}") String ccdGwTotpSecret,
                     @Value("${s2s.api.ccdGwServiceName}") String ccdGwMicroserviceName,
                     S2sApi s2sApi) {
        this.ccdGwTotpSecret = ccdGwTotpSecret;
        this.ccdGwMicroserviceName = ccdGwMicroserviceName;
        this.s2sApi = s2sApi;
    }

    public String getCcdGwS2sToken() {
        return getS2sToken(ccdGwMicroserviceName, ccdGwTotpSecret);
    }

    private String getS2sToken(String microserviceName, String microserviceSecret) {
        S2sApi.S2sPostBody s2sPostBody = new S2sApi.S2sPostBody(
                microserviceName,
                String.valueOf(new GoogleAuthenticator().getTotpPassword(microserviceSecret)));

        return s2sApi.generateToken(s2sPostBody);

    }

}
