package com.malt.hiringexercise.commission

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.time.Period

class CommissionControllerTest {

    private val commissionServiceMock = mock(CommissionService::class.java)
    private val commissionController = CommissionController(commissionServiceMock)

    @Test
    fun `get commission successful`() {
        // Given.
        val commissionRateRequestDTO = createCommissionRateRequest()
        val commissionRateResponseDTO = CommissionRateResponseDTO(10)
        whenever(commissionServiceMock.computeCommission(commissionRateRequestDTO)).thenReturn(commissionRateResponseDTO)

        // When.
        val actual = commissionController.getCommission(commissionRateRequestDTO)

        // Then.
        verify(commissionServiceMock).computeCommission(commissionRateRequestDTO)
        assertThat(actual).isEqualTo(commissionRateResponseDTO)
    }

    @Test
    fun `add rule successful`() {
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
        commissionController.addRule(createCommissionRuleDTO)

        // Then.
        verify(commissionServiceMock).create(createCommissionRuleDTO)
    }

    companion object {
        fun createCommissionRateRequest(
            customerIp: String = "8.8.8.8",
            freelanceIp: String = "8.8.8.9",
            missionLength: String = "P1M",
            firstMission: LocalDateTime = LocalDateTime.of(2020, 5, 5, 8, 10),
            lastMission: LocalDateTime? = LocalDateTime.of(2020, 5, 5, 8, 10)
        ): CommissionRateRequestDTO {
            return CommissionRateRequestDTO(
                Customer(customerIp),
                Freelancer(freelanceIp),
                Mission(missionLength),
                CommercialRelation(firstMission, lastMission)
            )
        }
    }
}
