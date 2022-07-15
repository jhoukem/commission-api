package com.malt.hiringexercise

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.streams.toList

/**
 * An example of how you could handle the "data part" of the exercise.
 */
@Component
class InMemoryStore {
    private val things: MutableList<String> = ArrayList()

    @EventListener(ApplicationReadyEvent::class)
    fun storeSomeThings(e: ApplicationReadyEvent?) {
        addThing("foo")
        addThing("bar")
        addThing("foobar")
    }

    fun addThing(newThing: String) {
        things.add(newThing)
    }

    fun findThingsHavingNameStartingWith(namePrefix: String?): List<String> {
        return things.stream()
            .filter { n: String -> n.startsWith(namePrefix!!) }
            .toList()
    }
}