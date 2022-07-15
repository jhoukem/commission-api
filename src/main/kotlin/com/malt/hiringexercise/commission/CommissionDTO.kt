package com.malt.hiringexercise.commission

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
data class CommercialRelation(val firstMission: LocalDateTime, val lastMission: LocalDateTime?)


data class CommissionRateResponseDTO(
    val fees: Int,
    val reason: String? = null,
)


