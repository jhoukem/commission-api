package com.malt.hiringexercise.commission

import com.malt.hiringexercise.persistence.CommissionRuleRepository
import org.springframework.stereotype.Service

@Service
class CommissionService constructor(
    private val repository: CommissionRuleRepository,
    private val commissionRestrictionService: CommissionRestrictionService
) {

    fun computeCommission(commissionRateRequestDTO: CommissionRateRequestDTO): CommissionRateResponseDTO {
        val rules = repository.findAllRules()

        // Gets the first rule that matches the restriction.
        val commissionRule = rules.firstOrNull { rule ->
            commissionRestrictionService.validate(rule.restriction, commissionRateRequestDTO)
        } ?: return DEFAULT_COMMISSION_RATE

        return CommissionRateResponseDTO(commissionRule.rate.percent, commissionRule.name)
    }

    fun create(createCommissionRuleDTO: CreateCommissionRuleDTO) {
        val commissionRule = CommissionRule(
            createCommissionRuleDTO.name,
            Rate(createCommissionRuleDTO.rate),
            createCommissionRuleDTO.restriction
        )
        repository.add(commissionRule)
    }

    companion object {
        val DEFAULT_COMMISSION_RATE = CommissionRateResponseDTO(10)
    }
}

