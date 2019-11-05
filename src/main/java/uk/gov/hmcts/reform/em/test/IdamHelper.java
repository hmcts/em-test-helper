package uk.gov.hmcts.reform.em.test;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.IdamTestApi;
import uk.gov.hmcts.reform.idam.client.models.test.CreateUserRequest;
import uk.gov.hmcts.reform.idam.client.models.test.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IdamHelper {

    private final IdamClient idamClient;

    private final IdamTestApi idamTestApi;

    private final DeleteUserApi deleteUserApi;

    private final String password = "password";

    private final Map<String, String> idamTokens = new HashMap<>();

    @Autowired
    public IdamHelper(IdamClient idamClient, IdamTestApi idamTestApi, DeleteUserApi deleteUserApi) {
        this.idamClient = idamClient;
        this.idamTestApi = idamTestApi;
        this.deleteUserApi = deleteUserApi;
    }

    public String authenticateUser(String username, List<String> roles) {

        if (!idamTokens.containsKey(username)) {
            try {
                deleteUserApi.deleteUser(username);
            } catch (FeignException.NotFound e) {
                //DO NOTHING
            }

            idamTestApi.createUser(CreateUserRequest.builder().email(username).password(password)
                    .roles(roles.stream().map(UserRole::new).collect(Collectors.toList())).build());

            String code = idamClient.authenticateUser(username, password);

            idamTokens.put(username, code);
        }
        return idamTokens.get(username);
    }

    public void deleteUser(String username) {
        deleteUserApi.deleteUser(username);
    }

    public String getUserId(String username) {
        return idamClient.getUserDetails(getAuthenticatedUserIdamToken(username)).getId();
    }

    public String getAuthenticatedUserIdamToken(String username) {
        return idamTokens.get(username);
    }


}
