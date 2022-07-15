package com.malt.hiringexercise.commissionrate

import com.malt.hiringexercise.persistence.RuleRepository
import org.springframework.stereotype.Service

@Service
class CommissionRateService constructor(
    private val repository: RuleRepository,
    private val commissionRuleService: CommissionRuleService
) {

    fun computeCommission(commissionRateRequestDTO: CommissionRateRequestDTO): CommissionRateResponseDTO {
        val rules = repository.findAllRules()

        // Gets the first rule that matches the restriction.
        val commissionRule = rules.firstOrNull { rule ->
            commissionRuleService.validate(rule.restriction, commissionRateRequestDTO)
        } ?: return DEFAULT_COMMISSION_RATE

        return CommissionRateResponseDTO(commissionRule.rate.percent, commissionRule.name);
    }

    companion object {
        val DEFAULT_COMMISSION_RATE = CommissionRateResponseDTO(10, "default");
    }
}

