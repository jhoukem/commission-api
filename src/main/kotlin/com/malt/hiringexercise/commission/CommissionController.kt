package com.malt.hiringexercise.commission

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("commissions")
class CommissionController constructor(private val commissionService: CommissionService) {

    @PostMapping("rate/compute")
    fun getCommission(@RequestBody commissionRateRequestDTO: CommissionRateRequestDTO): ResponseEntity<CommissionRateResponseDTO> {
        val commission = commissionService.computeCommission(commissionRateRequestDTO);
        return ResponseEntity.ok(commission);
    }

    @PostMapping("rules")
    fun addRule(@RequestBody createCommissionRuleDTO: CreateCommissionRuleDTO): ResponseEntity<Void> {
        commissionService.create(createCommissionRuleDTO)
        return ResponseEntity.ok().build()
    }

}