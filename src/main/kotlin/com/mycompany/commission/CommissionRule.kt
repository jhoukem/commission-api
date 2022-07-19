package com.mycompany.commission

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.Period

data class CommissionRule(
    val name: String,
    val rate: Rate,
    val restriction: Restriction
)

data class Rate(val percent: Int)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
sealed class Restriction

@JsonTypeName("or")
data class Or(val restrictions: List<Restriction>) : Restriction()

@JsonTypeName("and")
data class And(val restrictions: List<Restriction>) : Restriction()

@JsonTypeName("mission_duration")
data class MissionDuration(val greaterThan: Period) : Restriction()

@JsonTypeName("commercial_duration")
data class CommercialRelationDuration(val greaterThan: Period) : Restriction()

@JsonTypeName("customer_location")
data class CustomerLocation(val countryCode: String) : Restriction()

@JsonTypeName("freelance_location")
data class FreelanceLocation(val countryCode: String) : Restriction()
