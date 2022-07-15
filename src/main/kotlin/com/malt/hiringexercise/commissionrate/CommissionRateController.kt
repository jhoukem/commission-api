package com.malt.hiringexercise.commissionrate

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("commissions")
class CommissionRateController constructor(private val commissionRateService: CommissionRateService) {

    @PostMapping("/rate/compute")
    fun getCommission(@RequestBody commissionRateRequestDTO: CommissionRateRequestDTO): ResponseEntity<CommissionRateResponseDTO> {
        return try {
            val commission = commissionRateService.computeCommission(commissionRateRequestDTO);
            ResponseEntity.ok(commission);
        } catch (e: Exception) {
            ResponseEntity.badRequest().build();
        }
    }

}