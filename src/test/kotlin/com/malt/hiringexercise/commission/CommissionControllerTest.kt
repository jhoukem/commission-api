package com.malt.hiringexercise.commission

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.time.Period

class CommissionControllerTest {

    private val commissionServiceMock = mock(CommissionService::class.java)
    private val commissionController = CommissionController(commissionServiceMock)

    @Test
    fun getCommission() {
        // Given.
        val commissionRateRequestDTO =
            createCommissionRateRequest("8.8.8.8", "8.8.8.9", "P1M", LocalDateTime.of(2020, 10, 5, 8, 10))
        val commissionRateResponseDTO = CommissionRateResponseDTO(10)
        `when`(commissionServiceMock.computeCommission(commissionRateRequestDTO))
            .thenReturn(commissionRateResponseDTO)

        // When.
        val actual = commissionController.getCommission(commissionRateRequestDTO)

        // Then.
        verify(commissionServiceMock).computeCommission(commissionRateRequestDTO)
        assertThat(actual).isEqualTo(commissionRateResponseDTO)
    }

    @Test
    fun addRule() {
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

    private fun createCommissionRateRequest(
        customerIp: String,
        freelanceIp: String,
        missionLength: String,
        firstMission: LocalDateTime,
        lastMission: LocalDateTime? = null
    ): CommissionRateRequestDTO {
        return CommissionRateRequestDTO(
            Customer(customerIp),
            Freelancer(freelanceIp),
            Mission(missionLength),
            CommercialRelation(firstMission, lastMission)
        )
    }
}