package com.malt.hiringexercise.persistence

import com.malt.hiringexercise.commissionrate.*
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Repository

@Repository
class InMemoryCommissionRuleRepository : CommissionRuleRepository {
    private val rules: MutableList<CommissionRule> = ArrayList()

    @EventListener(ApplicationReadyEvent::class)
    fun initializeRules(e: ApplicationReadyEvent) {
        val defaultRules = listOf(
            CommissionRule(
                "1", "Rule 1", Rate(5),
                Or(
                    listOf(
                        CustomerLocation("FR"), FreelanceLocation("FR")
                    )
                )
            ),
            CommissionRule(
                "2", "Rule 2", Rate(6),
                And(
                    listOf(
                        CustomerLocation("FR"), FreelanceLocation("FR")
                    )
                )
            )
        );

        rules.addAll(defaultRules);
    }

    override fun findAllRules(): List<CommissionRule> {
        return rules
    }

}