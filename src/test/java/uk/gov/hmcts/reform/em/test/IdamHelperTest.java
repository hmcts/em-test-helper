package uk.gov.hmcts.reform.em.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.em.idam.client.IdamClient;
import uk.gov.hmcts.reform.em.idam.client.IdamTestApi;
import uk.gov.hmcts.reform.em.idam.client.models.UserDetails;
import uk.gov.hmcts.reform.em.test.idam.DeleteUserApi;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;

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
        UserDetails userDetailsMock = mock(UserDetails.class);
        when(userDetailsMock.getId()).thenReturn("id");
        when(idamClient.authenticateUser(any(), any())).thenReturn("a");
        when(idamClient.getUserDetails("a")).thenReturn(userDetailsMock);
        assertThat(idamHelper.getUserId("x")).isEqualTo("id");
    }

    @Test
    public void testDeleteUser() {
        idamHelper.deleteUser("x");
        verify(deleteUserApi, times(1)).deleteUser("x");
    }

    @Test
    public void testAuthenticateUser() {
        when(idamClient.authenticateUser(any(), any())).thenReturn("a");
        assertThat(idamHelper.authenticateUser("x")).isEqualTo("a");
        assertThat(idamHelper.authenticateUser("x")).isEqualTo("a");
        assertThat(idamHelper.authenticateUser("x")).isEqualTo("a");
        verify(idamClient, times(1)).authenticateUser(any(), any());
    }


}
