package com.mycompany.commission

import com.mycompany.commission.CommissionControllerTest.Companion.createCommissionRateRequest
import com.mycompany.geoip.GeoIpService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.time.Period

class CommissionRestrictionServiceTest {

    private val geoIpServiceMock: GeoIpService = mock()
    private val commissionRestrictionService = CommissionRestrictionService(geoIpServiceMock)

    @BeforeEach
    fun cleanMocks() {
        reset(geoIpServiceMock)
    }

    @Test
    fun `validate customer location restriction`() {
        // Given.
        val commissionRateRequestDTO = createCommissionRateRequest()
        val restriction = CustomerLocation("ES")
        whenever(geoIpServiceMock.getCountryCode(any())).thenReturn("ES").thenReturn("FR")

        // When.
        val actual1 = commissionRestrictionService.validate(restriction, commissionRateRequestDTO)
        val actual2 = commissionRestrictionService.validate(restriction, commissionRateRequestDTO)

        // Then.
        verify(geoIpServiceMock, times(2)).getCountryCode("8.8.8.8")
        assertThat(actual1).isTrue
        assertThat(actual2).isFalse
    }

    @Test
    fun `validate freelance location restriction`() {
        // Given.
        val commissionRateRequestDTO = createCommissionRateRequest()
        val restriction = FreelanceLocation("IT")
        whenever(geoIpServiceMock.getCountryCode(any())).thenReturn("ES").thenReturn("IT")

        // When.
        val actual1 = commissionRestrictionService.validate(restriction, commissionRateRequestDTO)
        val actual2 = commissionRestrictionService.validate(restriction, commissionRateRequestDTO)

        // Then.
        verify(geoIpServiceMock, times(2)).getCountryCode("8.8.8.9")
        assertThat(actual1).isFalse
        assertThat(actual2).isTrue
    }

    @Test
    fun `validate mission duration restriction`() {
        // Given.
        val mission1MonthRequest = createCommissionRateRequest(missionLength = "P1M")
        val mission3MonthRequest = createCommissionRateRequest(missionLength = "P3M")
        val restriction = MissionDuration(Period.of(0, 2, 0))

        // When.
        val actual1MonthResult = commissionRestrictionService.validate(restriction, mission1MonthRequest)
        val actual3MonthResult = commissionRestrictionService.validate(restriction, mission3MonthRequest)

        // Then.
        verify(geoIpServiceMock, times(0)).getCountryCode(any())
        assertThat(actual1MonthResult).`as`("1 month mission duration should not validate the 3 months mission duration restriction").isFalse
        assertThat(actual3MonthResult).`as`("3 months mission duration should validate the 3 months mission duration restriction").isTrue
    }

    @Test
    fun `validate commercial relation restriction`() {
        // Given.
        val commercialRelation1MonthRequest = createCommissionRateRequest(
            firstMission = LocalDateTime.of(2020, 5, 10, 8, 0),
            lastMission = LocalDateTime.of(2020, 6, 10, 8, 0)
        )
        val commercialRelation3MonthRequest = createCommissionRateRequest(
            firstMission = LocalDateTime.of(2020, 5, 10, 8, 0),
            lastMission = LocalDateTime.of(2020, 8, 10, 8, 0)
        )
        val restriction = CommercialRelationDuration(Period.of(0, 2, 0))

        // When.
        val actual1MonthDuration = commissionRestrictionService.validate(restriction, commercialRelation1MonthRequest)
        val actual3MonthDuration = commissionRestrictionService.validate(restriction, commercialRelation3MonthRequest)

        // Then.
        verify(geoIpServiceMock, times(0)).getCountryCode(any())
        assertThat(actual1MonthDuration).`as`("1 month commercial duration should not validate the 3 months commercial duration restriction").isFalse
        assertThat(actual3MonthDuration).`as`("3 months commercial duration should validate the 3 months commercial duration restriction").isTrue
    }

    @Test
    fun `validate and relation restriction`() {
        // Given.
        val badRequest = createCommissionRateRequest(
            missionLength = "P1M",
            firstMission = LocalDateTime.of(2020, 5, 10, 8, 0),
            lastMission = LocalDateTime.of(2022, 8, 10, 8, 0)
        )
        val validRequest = createCommissionRateRequest(
            missionLength = "P2M",
            firstMission = LocalDateTime.of(2020, 5, 10, 8, 0),
            lastMission = LocalDateTime.of(2022, 8, 10, 8, 0)
        )
        val restriction = And(
            listOf(
                CommercialRelationDuration(Period.of(1, 0, 0)),// A commercial duration of 1 year
                MissionDuration(Period.of(0, 2, 0)) // and 2 months mission duration.
            )
        )

        // When.
        val badRequestResult = commissionRestrictionService.validate(restriction, badRequest)
        val validRequestResult = commissionRestrictionService.validate(restriction, validRequest)

        // Then.
        verify(geoIpServiceMock, times(0)).getCountryCode(any())
        assertThat(badRequestResult).`as`("The badRequest should not validate the current restriction").isFalse
        assertThat(validRequestResult).`as`("The goodRequest should validate the current restriction").isTrue
    }

    @Test
    fun `validate or relation restriction`() {
        // Given.
        val badRequest = createCommissionRateRequest(
            missionLength = "P1M",
            firstMission = LocalDateTime.of(2020, 5, 10, 8, 0),
            lastMission = LocalDateTime.of(2020, 8, 10, 8, 0)
        )
        val validRequest = createCommissionRateRequest(
            missionLength = "P1M",
            firstMission = LocalDateTime.of(2020, 5, 10, 8, 0),
            lastMission = LocalDateTime.of(2022, 8, 10, 8, 0)
        )
        val restriction = Or(
            listOf(
                CommercialRelationDuration(Period.of(1, 0, 0)),// A commercial duration of 1 year
                MissionDuration(Period.of(0, 2, 0)) // or 2 months mission duration.
            )
        )

        // When.
        val badRequestResult = commissionRestrictionService.validate(restriction, badRequest)
        val validRequestResult = commissionRestrictionService.validate(restriction, validRequest)

        // Then.
        verify(geoIpServiceMock, times(0)).getCountryCode(any())
        assertThat(badRequestResult).`as`("The badRequest should not validate the current restriction").isFalse
        assertThat(validRequestResult).`as`("The goodRequest should validate the current restriction").isTrue
    }
}