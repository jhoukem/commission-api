package com.mycompany.commission

import java.time.LocalDateTime

data class CommissionRateRequestDTO(
    val customer: Customer,
    val freelancer: Freelancer,
    val mission: Mission,
    val commercialRelation: CommercialRelation?
)

data class Customer(val ip: String)
data class Freelancer(val ip: String)
data class Mission(val length: String)
data class CommercialRelation(val firstMission: LocalDateTime, val lastMission: LocalDateTime?) {
    init {
        lastMission?.let {
            require(it.isEqual(firstMission) || it.isAfter(firstMission)) { "The lastMission date must be after the firstMission date" }
        }
    }
}

data class CommissionRateResponseDTO(
    val fees: Int,
    val reason: String? = null,
)

data class CreateCommissionRuleDTO(
    val name: String,
    val rate: Int,
    val restriction: Restriction
)