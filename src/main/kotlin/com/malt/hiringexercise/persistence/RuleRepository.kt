package com.malt.hiringexercise.persistence

import com.malt.hiringexercise.commissionrate.CommissionRule

interface RuleRepository {
    fun findAllRules(): List<CommissionRule>
}