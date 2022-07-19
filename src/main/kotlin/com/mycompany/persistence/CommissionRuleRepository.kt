package com.mycompany.persistence

import com.mycompany.commission.CommissionRule

interface CommissionRuleRepository {
    fun findAllRules(): List<CommissionRule>
    fun add(rule: CommissionRule)
}