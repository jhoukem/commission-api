package com.mycompany.geoip

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kong.unirest.Unirest
import org.springframework.stereotype.Service

@Service
class IpStackService : GeoIpService {

    private final val mapper: ObjectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun getCountryCode(ip: String): String? {

        val httpResponse = Unirest.get("http://api.ipstack.com/{ip}")
            .header("accept", "application/json")
            .routeParam("ip", ip)
            .queryString("access_key", API_KEY)
            .asString()

        if (!httpResponse.isSuccess) {
            return null
        }

        return try {
            val ipStackResponse = mapper.readValue<IpStackResponse>(httpResponse.body)
            ipStackResponse.country_code
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        const val API_KEY = "96a05a43cccabe157f9ae9210382a375"
    }
}

data class IpStackResponse(val country_code: String)
