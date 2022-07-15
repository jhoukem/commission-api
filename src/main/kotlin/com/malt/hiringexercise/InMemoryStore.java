package com.malt.hiringexercise;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * An example of how you could handle the "data part" of the exercise.
 */
@Component
public class InMemoryStore {

    private final List<String> things = new ArrayList<>();

    @EventListener(ApplicationReadyEvent.class)
    public void storeSomeThings(ApplicationReadyEvent e) {
        addThing("foo");
        addThing("bar");
        addThing("foobar");
    }

    public void addThing(String newThing) {
        things.add(newThing);
    }

    public List<String> findThingsHavingNameStartingWith(String namePrefix) {
        return things.stream()
                .filter(n -> n.startsWith(namePrefix))
                .toList();
    }
}
