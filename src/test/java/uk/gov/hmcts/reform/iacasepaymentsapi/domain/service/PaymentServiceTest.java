package uk.gov.hmcts.reform.iacasepaymentsapi.domain.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator;
import uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.fee.Fee;
import uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.payment.CreditAccountPayment;
import uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.payment.Currency;
import uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.payment.PaymentResponse;
import uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.payment.StatusHistories;
import uk.gov.hmcts.reform.iacasepaymentsapi.infrastructure.clients.PaymentApi;
import uk.gov.hmcts.reform.iacasepaymentsapi.infrastructure.config.PaymentConfiguration;
import uk.gov.hmcts.reform.iacasepaymentsapi.infrastructure.security.RequestUserAccessTokenProvider;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
public class PaymentServiceTest {

    @Mock private PaymentApi paymentApi;
    @Mock private PaymentConfiguration paymentConfiguration;
    @Mock private RequestUserAccessTokenProvider userAuthorizationProvider;
    @Mock private AuthTokenGenerator serviceAuthorizationProvider;

    private CreditAccountPayment creditAccountPayment;

    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {

        paymentService = new PaymentService(
            paymentApi,
            paymentConfiguration,
            userAuthorizationProvider,
            serviceAuthorizationProvider);
    }

    @Test
    public void should_make_a_pba_payment() throws Exception {

        when(userAuthorizationProvider.getAccessToken()).thenReturn("userAuthorizationToken");
        when(serviceAuthorizationProvider.generate()).thenReturn("serviceAuthorizationToken");

        creditAccountPayment = getCreditAccountPaymentRequest();
        when(paymentApi.creditAccountPaymentRequest(
            userAuthorizationProvider.getAccessToken(),
            serviceAuthorizationProvider.generate(),
            creditAccountPayment
            ))
            .thenReturn(new PaymentResponse("RC-1590-6748-2373-9129", new Date(),
            "Success", "2020-1590674823325",
                Arrays.asList(new StatusHistories("Success", null, null))));


        PaymentResponse paymentResponse = paymentService.creditAccountPayment(creditAccountPayment);

        assertNotNull(paymentResponse);

        assertEquals(paymentResponse.getReference(), "RC-1590-6748-2373-9129");
        assertEquals(paymentResponse.getStatus(), "Success");
    }

    @Test
    public void should_throw_for_null_request() {

        assertThatThrownBy(() -> paymentService.creditAccountPayment(null))
            .isExactlyInstanceOf(NullPointerException.class);
    }

    private CreditAccountPayment getCreditAccountPaymentRequest() throws Exception {

        return new CreditAccountPayment("PBA0066906", new BigDecimal("140.00"),
            "caseReference", "ccdCaseNumber",
            Currency.GBP, "customerReference", "Some description",
            Arrays.asList(new Fee("FEE0123", "Fee description", 1, new BigDecimal("140.00"))));
    }
}
