package com.malt.hiringexercise.commission

import com.malt.hiringexercise.commission.CommissionControllerTest.Companion.createCommissionRateRequest
import com.malt.hiringexercise.persistence.CommissionRuleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.time.Period

class CommissionServiceTest {

    private val repositoryMock: CommissionRuleRepository = mock()
    private val restrictionServiceMock: CommissionRestrictionService = mock()
    private val commissionService = CommissionService(repositoryMock, restrictionServiceMock)

    @Test
    fun `compute commission with rule matching`() {
        // Given.
        val commissionRateRequestDTO =
            createCommissionRateRequest("8.8.8.8", "8.8.8.9", "P1M", LocalDateTime.of(2020, 10, 5, 8, 10))
        whenever(repositoryMock.findAllRules()).thenReturn(
            listOf(
                CommissionRule("rule1", Rate(8), CommercialRelationDuration(Period.of(1, 0, 0))),
                CommissionRule("rule2", Rate(18), CommercialRelationDuration(Period.of(10, 0, 0)))
            )
        )
        // Matches the second rule.
        whenever(restrictionServiceMock.validate(any(), any())).thenReturn(false).thenReturn(true)

        // When.
        val actual = commissionService.computeCommission(commissionRateRequestDTO)

        // Then.
        assertThat(actual).isEqualTo(CommissionRateResponseDTO(18, "rule2"))
    }

    @Test
    fun `compute commission without rule matching`() {
        // Given.
        val commissionRateRequestDTO =
            createCommissionRateRequest("8.8.8.8", "8.8.8.9", "P1M", LocalDateTime.of(2020, 10, 5, 8, 10))
        given(repositoryMock.findAllRules()).willReturn(
            listOf(
                CommissionRule("rule1", Rate(8), CommercialRelationDuration(Period.of(1, 0, 0))),
                CommissionRule("rule2", Rate(18), CommercialRelationDuration(Period.of(10, 0, 0)))
            )
        )
        // Matches no rules.
        whenever(restrictionServiceMock.validate(any(), any())).thenReturn(false)

        // When.
        val actual = commissionService.computeCommission(commissionRateRequestDTO)

        // Then.
        assertThat(actual).isEqualTo(CommissionRateResponseDTO(10))
    }

    @Test
    fun create() {
        // Given.
        val createCommissionRuleDTO = CreateCommissionRuleDTO(
            "rule1",
            8,
            And(
                listOf(
                    CommercialRelationDuration(Period.of(0, 1, 0)),
                    CustomerLocation("FR")
                )
            )
        )

        // When.
        commissionService.create(createCommissionRuleDTO)

        // Then.
        verify(repositoryMock).add(
            CommissionRule(
                "rule1",
                Rate(8),
                And(
                    listOf(
                        CommercialRelationDuration(Period.of(0, 1, 0)),
                        CustomerLocation("FR")
                    )
                )
            )
        )
    }

}
