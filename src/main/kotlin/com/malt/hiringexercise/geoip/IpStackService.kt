package com.malt.hiringexercise.geoip

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

@Service
class IpStackService : GeoIpService {

    private final val httpClient: HttpClient = HttpClient.newHttpClient()
    private final val mapper: ObjectMapper = jacksonObjectMapper()

    override fun getCountryCode(ip: String): String? {
        val request = HttpRequest.newBuilder(URI.create("https://api.ipstack.com/$ip?access_key=$API_KEY"))
            .GET()
            .build()
        val response = httpClient.send(request, BodyHandlers.ofString())

        if (response.statusCode() != HttpStatus.OK.value()) {
            return null
        }

        return try {
            val ipStackResponse = mapper.readValue<IpStackResponse>(response.body())
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
