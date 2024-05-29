package uk.gov.hmcts.reform.em.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.em.test.idam.DeleteUserApi;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.idam.OpenIdConfiguration;
import uk.gov.hmcts.reform.em.test.idam.OpenIdUserApi;
import uk.gov.hmcts.reform.em.test.idam.client.models.OpenIdAuthUserRequest;
import uk.gov.hmcts.reform.em.test.idam.client.models.OpenIdAuthUserResponse;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.IdamTestApi;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IdamHelperTest {

    @Mock
    IdamClient idamClient;

    @Mock
    IdamTestApi idamTestApi;

    @Mock
    DeleteUserApi deleteUserApi;

    @Mock
    OpenIdUserApi openIdUserApi;

    @Mock
    OpenIdConfiguration openIdConfiguration;

    @Mock
    OpenIdAuthUserRequest openIdAuthUserRequest;

    @Mock
    OpenIdAuthUserResponse openIdAuthUserResponse;

    @InjectMocks
    private IdamHelper idamHelper;

    @Test
    public void testCreateUser() {
        idamHelper.createUser("x", Stream.of("x").collect(Collectors.toList()));
        verify(deleteUserApi, times(1)).deleteUser("x");
        verify(idamTestApi, times(1)).createUser(any());
    }

    @Test
    public void testGetUserId() {
        UserInfo userDetailsMock = mock(UserInfo.class);
        when(userDetailsMock.getUid()).thenReturn("id");
        when(openIdAuthUserResponse.getAccessToken()).thenReturn("b");
        when(openIdUserApi.authenticateUser(any())).thenReturn(openIdAuthUserResponse);
        when(idamClient.getUserInfo("Bearer b")).thenReturn(userDetailsMock);
        assertThat(idamHelper.getUserId("x")).isEqualTo("id");
    }

    @Test
    public void testDeleteUser() {
        idamHelper.deleteUser("x");
        verify(deleteUserApi, times(1)).deleteUser("x");
    }

    @Test
    public void testAuthenticateUser() {
        when(openIdAuthUserResponse.getAccessToken()).thenReturn("b");
        when(openIdUserApi.authenticateUser(any())).thenReturn(openIdAuthUserResponse);
        assertThat(idamHelper.authenticateUser("x")).isEqualTo("Bearer b");
        assertThat(idamHelper.authenticateUser("x")).isEqualTo("Bearer b");
        assertThat(idamHelper.authenticateUser("x")).isEqualTo("Bearer b");
        verify(openIdUserApi, times(1)).authenticateUser(any());
    }


}
