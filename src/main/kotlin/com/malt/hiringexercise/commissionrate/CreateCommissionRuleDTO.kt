package com.malt.hiringexercise.commissionrate


data class CreateCommissionRuleDTO(
    val name: String,
    val rate: Int,
    val restriction: Restriction
)