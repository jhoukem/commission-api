package com.malt.hiringexercise.commission


data class CreateCommissionRuleDTO(
    val name: String,
    val rate: Int,
    val restriction: Restriction
)