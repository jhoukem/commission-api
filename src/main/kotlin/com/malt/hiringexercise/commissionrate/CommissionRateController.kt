package com.malt.hiringexercise.commissionrate

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("commissions")
class CommissionRateController {


    @PostMapping()
    fun getCommission(@RequestBody commissionRateRequestDTO: CommissionRateRequestDTO): ResponseEntity<CommissionRateResponseDTO> {
        return ResponseEntity.ok(null);
    }

}