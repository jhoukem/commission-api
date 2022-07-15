package com.malt.hiringexercise.geoip

import org.springframework.stereotype.Service

@Service
class IpStackService : GeoIpService {
    override fun getCountryCode(ip: String): String {
        return "FR"
    }
}