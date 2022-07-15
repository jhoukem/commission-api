package com.malt.hiringexercise

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val context = runApplication<Application>(*args)
    val port = context.environment.getProperty("local.server.port")
    println("""
        
        Everything's fine! Open this link to have the server return something ;-)
        http://localhost:$port
        
    """.trimIndent())
}
