package com.malt.hiringexercise.commissionrate

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("commissions")
class CommissionRateController constructor(private val commissionRateService: CommissionRateService) {

    @PostMapping("rate/compute")
    fun getCommission(@RequestBody commissionRateRequestDTO: CommissionRateRequestDTO): ResponseEntity<CommissionRateResponseDTO> {
        val commission = commissionRateService.computeCommission(commissionRateRequestDTO);
        return ResponseEntity.ok(commission);
    }

    @PostMapping("rules")
    fun addRule(@RequestBody createCommissionRateRuleDTO: CreateCommissionRuleDTO): ResponseEntity<Void> {
        commissionRateService.create(createCommissionRateRuleDTO)
        return ResponseEntity.ok().build()
    }

}