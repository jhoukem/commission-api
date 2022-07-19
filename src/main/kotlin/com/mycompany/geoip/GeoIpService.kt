package com.mycompany.geoip

interface GeoIpService {
    fun getCountryCode(ip: String): String?
}