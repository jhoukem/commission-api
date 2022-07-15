package com.malt.hiringexercise.commissionrate

import com.malt.hiringexercise.geoip.GeoIpService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
class CommissionRuleService constructor(private val geoIpService: GeoIpService) {

    fun validate(restriction: Restriction, commissionRateRequestDTO: CommissionRateRequestDTO): Boolean {
        return when (restriction) {
            is And -> andRestrictionHandler(restriction, commissionRateRequestDTO)
            is Or -> orRestrictionHandler(restriction, commissionRateRequestDTO)
            is MissionDuration -> missionDurationHandler(restriction, commissionRateRequestDTO)
            is CommercialRelationDuration -> commercialRelationDurationHandler(restriction, commissionRateRequestDTO)
            is CustomerLocation -> customerLocationHandler(restriction, commissionRateRequestDTO)
            is FreelanceLocation -> freelanceLocationHandler(restriction, commissionRateRequestDTO)
        }
    }

    private fun andRestrictionHandler(restriction: And, commissionRateRequestDTO: CommissionRateRequestDTO): Boolean {
        return restriction.restrictions.stream().allMatch { r -> validate(r, commissionRateRequestDTO) }
    }

    private fun orRestrictionHandler(restriction: Or, commissionRateRequestDTO: CommissionRateRequestDTO): Boolean {
        return restriction.restrictions.stream().anyMatch { r -> validate(r, commissionRateRequestDTO) }
    }

    private fun missionDurationHandler(
        restriction: MissionDuration,
        commissionRateRequestDTO: CommissionRateRequestDTO
    ): Boolean {
        val missionDuration = stringToPeriod(commissionRateRequestDTO.mission.length) ?: return false
        return compareTo(missionDuration, restriction.greaterThan) > 0;
    }

    private fun commercialRelationDurationHandler(
        restriction: CommercialRelationDuration,
        commissionRateRequestDTO: CommissionRateRequestDTO
    ): Boolean {
        val firstMission = commissionRateRequestDTO.commercialRelation.firstMission ?: return false
        val lastMission = commissionRateRequestDTO.commercialRelation.lastMission ?: return false

        val commercialRelationDuration = Period.between(firstMission.toLocalDate(), lastMission.toLocalDate());
        return compareTo(commercialRelationDuration, restriction.greaterThan) > 0;
    }

    private fun stringToPeriod(periodAsString: String): Period? {
        return try {
            Period.parse(periodAsString);
        } catch (e: Exception) {
            null
        }
    }

    private fun compareTo(p1: Period, p2: Period): Int {
        val now = LocalDate.now()
        return now.plus(p1).compareTo(now.plus(p2))
    }

    private fun customerLocationHandler(
        restriction: CustomerLocation,
        commissionRateRequestDTO: CommissionRateRequestDTO
    ): Boolean {
        val customerCountryCode = geoIpService.getCountryCode(commissionRateRequestDTO.customer.ip)
        return restriction.countryCode == customerCountryCode;
    }

    private fun freelanceLocationHandler(
        restriction: FreelanceLocation,
        commissionRateRequestDTO: CommissionRateRequestDTO
    ): Boolean {
        val customerCountryCode = geoIpService.getCountryCode(commissionRateRequestDTO.freelancer.ip)
        return restriction.countryCode == customerCountryCode;
    }
}
