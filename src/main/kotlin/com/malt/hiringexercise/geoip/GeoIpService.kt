package com.malt.hiringexercise.geoip

interface GeoIpService {
    fun getCountryCode(ip: String): String
}