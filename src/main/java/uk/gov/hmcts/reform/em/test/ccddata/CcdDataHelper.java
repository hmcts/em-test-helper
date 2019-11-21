package uk.gov.hmcts.reform.em.test.ccddata;

import uk.gov.hmcts.reform.ccd.client.CoreCaseDataApi;
import uk.gov.hmcts.reform.ccd.client.model.CaseDataContent;
import uk.gov.hmcts.reform.ccd.client.model.CaseDetails;
import uk.gov.hmcts.reform.ccd.client.model.Event;
import uk.gov.hmcts.reform.ccd.client.model.StartEventResponse;
import uk.gov.hmcts.reform.em.test.idam.IdamHelper;
import uk.gov.hmcts.reform.em.test.s2s.S2sHelper;

public class CcdDataHelper {

    private final IdamHelper idamHelper;
    private final S2sHelper s2sHelper;
    private final CoreCaseDataApi coreCaseDataApi;

    public CcdDataHelper(IdamHelper idamHelper, S2sHelper s2sHelper, CoreCaseDataApi coreCaseDataApi) {
        this.idamHelper = idamHelper;
        this.s2sHelper = s2sHelper;
        this.coreCaseDataApi = coreCaseDataApi;
    }

    public CaseDetails createCase(String username, String jurisdiction, String caseType, String eventId, Object data) {

        final String userAuthorization = idamHelper.authenticateUser(username);
        final String s2sAuthorization = s2sHelper.getS2sToken();

        StartEventResponse startEventResponse = coreCaseDataApi.startCase(
                userAuthorization,
                s2sAuthorization,
                caseType,
                eventId);

        CaseDetails caseDetails = coreCaseDataApi.submitForCaseworker(
                userAuthorization,
                s2sAuthorization,
                idamHelper.getUserId(username),
                jurisdiction,
                caseType,
                false,
                CaseDataContent.builder()
                        .event(Event.builder().id(startEventResponse.getEventId()).build())
                        .eventToken(startEventResponse.getToken())
                        .data(data).build());

        return caseDetails;

    }

    public CaseDetails getCase(String username, String caseId) {
        final String userAuthorization = idamHelper.authenticateUser(username);
        final String s2sAuthorization = s2sHelper.getS2sToken();
        return coreCaseDataApi.getCase(userAuthorization, s2sAuthorization, caseId);
    }

}
