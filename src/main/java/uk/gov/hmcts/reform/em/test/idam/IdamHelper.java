package uk.gov.hmcts.reform.em.test.idam;

import feign.FeignException;
import uk.gov.hmcts.reform.em.test.idam.client.models.OpenIdAuthUserRequest;
import uk.gov.hmcts.reform.em.test.idam.client.models.OpenIdAuthUserResponse;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.IdamTestApi;
import uk.gov.hmcts.reform.idam.client.models.test.CreateUserRequest;
import uk.gov.hmcts.reform.idam.client.models.test.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IdamHelper {

    private final IdamClient idamClient;

    private final IdamTestApi idamTestApi;

    private final DeleteUserApi deleteUserApi;

    private final OpenIdUserApi openIdUserApi;

    private final OpenIdConfiguration openIdConfiguration;

    private final String password = "4590fgvhbfgbDdffm3lk4j";

    private final Map<String, String> idamTokens = new HashMap<>();

    public IdamHelper(IdamClient idamClient, IdamTestApi idamTestApi, DeleteUserApi deleteUserApi,
                      OpenIdUserApi openIdUserApi, OpenIdConfiguration openIdConfiguration) {
        this.idamClient = idamClient;
        this.idamTestApi = idamTestApi;
        this.deleteUserApi = deleteUserApi;
        this.openIdUserApi = openIdUserApi;
        this.openIdConfiguration = openIdConfiguration;
    }

    public void createUser(String username, List<String> roles) {

        deleteUser(username);

        idamTestApi.createUser(CreateUserRequest.builder().email(username).password(password)
                .roles(roles.stream().map(UserRole::new).collect(Collectors.toList())).build());

    }

    public void deleteUser(String username) {
        try {
            idamTokens.remove(username);
            deleteUserApi.deleteUser(username);
        } catch (FeignException.NotFound e) {
            //DO NOTHING
        }
    }

    public String getUserId(String username) {
        return idamClient.getUserInfo(authenticateUser(username)).getUid();
    }

    public String authenticateUser(String username) {
        if (!idamTokens.containsKey(username)) {
            String code = authenticateOpenIdUser(username, password);
            idamTokens.put(username, code);
        }
        return idamTokens.get(username);
    }

    private String authenticateOpenIdUser(String username, String password) {

        OpenIdAuthUserRequest openIdAuthUserRequest = OpenIdAuthUserRequest.builder()
                .clientId(openIdConfiguration.getClientId())
                .clientSecret(openIdConfiguration.getClient_secret())
                .grant_type(openIdConfiguration.getGrantType())
                .redirectUri(openIdConfiguration.getRedirectUri())
                .scope(openIdConfiguration.getScope())
                .username(username)
                .password(password)
                .build();
        OpenIdAuthUserResponse openIdAuthUserResponse = openIdUserApi.authenticateUser(openIdAuthUserRequest);
        return "Bearer " + openIdAuthUserResponse.getAccessToken();
    }


}
