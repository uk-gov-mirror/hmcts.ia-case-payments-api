package uk.gov.hmcts.reform.iacasepaymentsapi.domain.entities;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class RemissionTypeTest {

    @Test
    void has_correct_values() {
        assertEquals("noRemission", RemissionType.NO_REMISSION.toString());
        assertEquals("hoWaiverRemission", RemissionType.HO_WAIVER_REMISSION.toString());
        assertEquals("helpWithFees", RemissionType.HELP_WITH_FEES.toString());
        assertEquals("exceptionalCircumstancesRemission", RemissionType.EXCEPTIONAL_CIRCUMSTANCES_REMISSION.toString());
    }

    @Test
    void if_this_test_fails_it_is_because_it_needs_updating_with_your_changes() {
        assertEquals(4, RemissionType.values().length);
    }
}
