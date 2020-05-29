package uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities.payment;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StatusHistoriesTest {

    private String status = "Failed";
    private String errorCode = "Account has been deleted";
    private String errorMessage = "Some message";

    private StatusHistories statusHistories;

    @BeforeEach
    public void setUp() {

        statusHistories = new StatusHistories(status, errorCode, errorMessage);
    }

    @Test
    public void should_hold_onto_values() {

        assertEquals(statusHistories.getStatus(), status);
        assertEquals(statusHistories.getErrorCode(), errorCode);
        assertEquals(statusHistories.getErrorMessage(), errorMessage);
    }

    @Test
    public void should_throw_required_field_exception() {

        statusHistories = new StatusHistories(null, null, null);

        assertThatThrownBy(statusHistories::getStatus)
            .isExactlyInstanceOf(NullPointerException.class);
    }
}
