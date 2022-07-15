package com.malt.hiringexercise.commission

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.Period

data class CommissionRule(
    val name: String,
    val rate: Rate,
    val restriction: Restriction
)

data class Rate(val percent: Int)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
sealed class Restriction
class Or(val restrictions: List<Restriction>) : Restriction()
class And(val restrictions: List<Restriction>) : Restriction()
class MissionDuration(val greaterThan: Period) : Restriction()
class CommercialRelationDuration(val greaterThan: Period) : Restriction()
class CustomerLocation(val countryCode: String) : Restriction()
class FreelanceLocation(val countryCode: String) : Restriction()
