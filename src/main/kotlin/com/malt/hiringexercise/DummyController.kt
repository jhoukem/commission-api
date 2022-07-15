package com.malt.hiringexercise

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * A dummy controller returning things totally unrelated to the exercise :-)
 */
@RestController
class DummyController(private val store: InMemoryStore) {

    @GetMapping("/")
    fun listSomeThings(): DummyResponse {
        val things = store.findThingsHavingNameStartingWith("foo")

        return DummyResponse(
            query = """Things having name starting with "foo".""",
            thingsFound = things
        )
    }
}

data class DummyResponse(
    val query: String,
    val thingsFound: List<String>
)
