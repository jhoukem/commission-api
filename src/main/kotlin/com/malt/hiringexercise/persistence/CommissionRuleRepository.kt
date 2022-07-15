package com.malt.hiringexercise.persistence

import com.malt.hiringexercise.commissionrate.CommissionRule

interface CommissionRuleRepository {
    fun findAllRules(): List<CommissionRule>
    fun add(rule: CommissionRule)
}