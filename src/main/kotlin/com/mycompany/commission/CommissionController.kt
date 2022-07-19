package com.mycompany.commission

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("commissions")
class CommissionController constructor(private val commissionService: CommissionService) {

    @PostMapping("rate/compute")
    fun getCommission(@RequestBody commissionRateRequestDTO: CommissionRateRequestDTO): CommissionRateResponseDTO {
        return commissionService.computeCommission(commissionRateRequestDTO)
    }

    @PostMapping("rules")
    fun addRule(@RequestBody createCommissionRuleDTO: CreateCommissionRuleDTO) {
        commissionService.create(createCommissionRuleDTO)
    }

}