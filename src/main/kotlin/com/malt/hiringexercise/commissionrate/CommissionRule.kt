package com.malt.hiringexercise.commissionrate

import java.time.Period

data class CommissionRule(
    val id: String,
    val name: String,
    val rate: Rate,
    val restriction: Restriction
)

data class Rate(val percent: Int)

sealed class Restriction
class Or(val restrictions: List<Restriction>) : Restriction()
class And(val restrictions: List<Restriction>) : Restriction()
class MissionDuration(val greaterThan: Period) : Restriction()
class CommercialRelationDuration(val greaterThan: Period) : Restriction()
class CustomerLocation(val countryCode: String) : Restriction()
class FreelanceLocation(val countryCode: String) : Restriction()