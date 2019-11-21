package uk.gov.hmcts.reform.em.test.idam;

import feign.FeignException;
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

    private final String password = "password";

    private final Map<String, String> idamTokens = new HashMap<>();

    public IdamHelper(IdamClient idamClient, IdamTestApi idamTestApi, DeleteUserApi deleteUserApi) {
        this.idamClient = idamClient;
        this.idamTestApi = idamTestApi;
        this.deleteUserApi = deleteUserApi;
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
        return idamClient.getUserDetails(authenticateUser(username)).getId();
    }

    public String authenticateUser(String username) {
        if (!idamTokens.containsKey(username)) {
            String code = idamClient.authenticateUser(username, password);
            idamTokens.put(username, code);
        }
        return idamTokens.get(username);
    }


}
