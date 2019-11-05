package uk.gov.hmcts.reform.em.test;

import com.warrenstrange.googleauth.GoogleAuthenticator;

public class S2sHelper {

    private final String s2sUrl;
    private final String emGwTotpSecret;
    private final String emGwMicroserviceName;
    private final String ccdGwTotpSecret;
    private final String ccdGwMicroserviceName;
    private final S2sApi s2sApi;

    public S2sHelper(String s2sUrl, String emGwTotpSecret, String emGwMicroserviceName, String ccdGwTotpSecret,
                     String ccdGwMicroserviceName, S2sApi s2sApi) {
        this.s2sUrl = s2sUrl;
        this.emGwTotpSecret = emGwTotpSecret;
        this.emGwMicroserviceName = emGwMicroserviceName;
        this.ccdGwTotpSecret = ccdGwTotpSecret;
        this.ccdGwMicroserviceName = ccdGwMicroserviceName;
        this.s2sApi = s2sApi;
    }

    public String getEmGwS2sToken() {
        return getS2sToken(emGwMicroserviceName, emGwTotpSecret);
    }

    public String getCcdGwS2sToken() {
        return getS2sToken(ccdGwMicroserviceName, ccdGwTotpSecret);
    }

    private String getS2sToken(String microserviceName, String microserviceSecret) {

        return s2sApi.generateToken(microserviceName,
                String.valueOf(new GoogleAuthenticator().getTotpPassword(microserviceSecret))).toString();

    }
}
