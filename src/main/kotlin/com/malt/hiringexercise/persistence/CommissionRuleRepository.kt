package com.malt.hiringexercise.persistence

import com.malt.hiringexercise.commission.CommissionRule

interface CommissionRuleRepository {
    fun findAllRules(): List<CommissionRule>
    fun add(rule: CommissionRule)
}