package com.mycompany.commission

import com.mycompany.geoip.GeoIpService
import com.mycompany.persistence.CommissionRuleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.time.Period

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommissionIT(@Autowired val testRestTemplate: TestRestTemplate) {

    // We only mock external services.
    @MockBean
    private lateinit var ruleRepositoryMock: CommissionRuleRepository

    @MockBean
    private lateinit var geoIpServiceMock: GeoIpService

    @BeforeEach
    fun cleanMocks() {
        reset(ruleRepositoryMock)
        reset(geoIpServiceMock)
    }

    @Test
    fun `get commission successful`() {
        // Given.
        val commissionRateRequestJson = createCommissionRateRequest(
            missionLength = "P3M"
        )
        whenever(geoIpServiceMock.getCountryCode(any())).thenReturn("IT")
        whenever(ruleRepositoryMock.findAllRules()).thenReturn(
            listOf(
                CommissionRule(
                    "rule both party fr",
                    Rate(8),
                    And(
                        listOf(CustomerLocation("FR"), FreelanceLocation("FR"))
                    )
                ),
                CommissionRule(
                    "mission duration or commercial relation > 3 months",
                    Rate(10),
                    Or(
                        listOf(
                            MissionDuration(Period.of(0, 3, 0)), CommercialRelationDuration(Period.of(0, 3, 0))
                        )
                    )
                )
            )
        )

        // When.
        val response = testRestTemplate
            .postForEntity(
                "/commissions/rate/compute",
                commissionRateRequestJson,
                CommissionRateResponseDTO::class.java
            )


        // Then.
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(
            CommissionRateResponseDTO(
                10,
                "mission duration or commercial relation > 3 months"
            )
        )
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
        val response = testRestTemplate
            .postForEntity(
                "/commissions/rules",
                createCommissionRuleDTO,
                Unit::class.java
            )

        // Then.
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        verify(ruleRepositoryMock).add(
            CommissionRule(
                createCommissionRuleDTO.name,
                Rate(createCommissionRuleDTO.rate),
                createCommissionRuleDTO.restriction
            )
        )
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
