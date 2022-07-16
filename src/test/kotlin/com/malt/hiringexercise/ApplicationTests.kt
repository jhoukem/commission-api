package com.malt.hiringexercise

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApplicationTests {

    @Test
    fun appStartsWithoutThrowing() {
        assertDoesNotThrow {
            val context = runApplication<Application>()
            context.stop()
        }
    }

}
