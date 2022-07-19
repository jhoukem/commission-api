package com.mycompany.commission

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import java.time.LocalDateTime
import java.time.Period

@WebMvcTest(CommissionController::class)
class CommissionControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var commissionServiceMock: CommissionService

    private val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Test
    fun `get commission successful`() {
        // Given.
        val commissionRateRequestDTO = createCommissionRateRequest()
        val commissionRateResponseDTO = CommissionRateResponseDTO(10)
        whenever(commissionServiceMock.computeCommission(commissionRateRequestDTO)).thenReturn(commissionRateResponseDTO)

        // When.
        val response = mockMvc
            .perform(
                post("/commissions/rate/compute")
                    .content(mapper.writeValueAsBytes(commissionRateRequestDTO))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn()
            .response

        // Then.
        verify(commissionServiceMock).computeCommission(commissionRateRequestDTO)
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
        val actualResponse = mapper.readValue(response.contentAsString, CommissionRateResponseDTO::class.java)
        assertThat(actualResponse).isEqualTo(commissionRateResponseDTO)
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
        val response = mockMvc
            .perform(
                post("/commissions/rules")
                    .content(mapper.writeValueAsBytes(createCommissionRuleDTO))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn()
            .response

        // Then.
        verify(commissionServiceMock).create(createCommissionRuleDTO)
        assertThat(response.status).isEqualTo(HttpStatus.OK.value())
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
