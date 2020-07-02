package uk.gov.hmcts.reform.iacasepaymentsapi.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.fee.OrganisationEntityResponse;
import uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.fee.OrganisationResponse;
import uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.fee.SuperUser;
import uk.gov.hmcts.reform.iacasepaymentsapi.infrastructure.clients.IdamApi;
import uk.gov.hmcts.reform.iacasepaymentsapi.infrastructure.clients.RefDataApi;
import uk.gov.hmcts.reform.iacasepaymentsapi.infrastructure.clients.model.idam.UserInfo;
import uk.gov.hmcts.reform.iacasepaymentsapi.infrastructure.security.RequestUserAccessTokenProvider;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class RefDataServiceTest {
    @Mock
    private RefDataApi refDataApi;
    @Mock
    private RequestUserAccessTokenProvider userAuthorizationProvider;
    @Mock
    private AuthTokenGenerator serviceAuthorizationProvider;
    @Mock
    private IdamApi idamApi;
    @Mock
    private UserInfo userInfo;

    private RefDataService refDataService;

    @BeforeEach
    public void setUp() {
        refDataService = new RefDataService(
                refDataApi,
                userAuthorizationProvider,
                serviceAuthorizationProvider,
                idamApi);
    }

    @Test
    void should_get_ref_data_organisation() {

        OrganisationEntityResponse organisationEntityResponse = new
            OrganisationEntityResponse("orgId", "orgName", "active",
                        "sraId", "sraReg", "CO123", "some-url",
                        new SuperUser("John", "Smith", "jsmith@test.com"),
                        Arrays.asList("PBA123456", "PBA765432"), "contactInfo");

        OrganisationResponse mockResponse = new OrganisationResponse(organisationEntityResponse);

        when(userAuthorizationProvider.getAccessToken()).thenReturn("some-user-token");
        when(idamApi.userInfo(any())).thenReturn(userInfo);
        when(userInfo.getEmail()).thenReturn("jsmith@test.com");
        when(refDataApi.findOrganisation(anyString(), any(), anyString())).thenReturn(
                mockResponse);

        OrganisationResponse orgResponse = refDataService.getOrganisationResponse();

        assertNotNull(orgResponse);
        assertEquals(mockResponse, orgResponse);

        assertEquals("orgId", mockResponse.getOrganisationEntityResponse().getOrganisationIdentifier());
        assertEquals("orgName", mockResponse.getOrganisationEntityResponse().getName());
        assertEquals("active", mockResponse.getOrganisationEntityResponse().getStatus());
        assertEquals("sraId", mockResponse.getOrganisationEntityResponse().getSraId());
        assertEquals("sraReg", mockResponse.getOrganisationEntityResponse().getSraRegulated());
        assertEquals("CO123", mockResponse.getOrganisationEntityResponse().getCompanyNumber());
        assertEquals("some-url", mockResponse.getOrganisationEntityResponse().getCompanyUrl());
        assertEquals("PBA123456", mockResponse.getOrganisationEntityResponse().getPaymentAccount().get(0));
        assertEquals("PBA765432", mockResponse.getOrganisationEntityResponse().getPaymentAccount().get(1));
        assertEquals("contactInfo", mockResponse.getOrganisationEntityResponse().getContactInformation());
    }
}