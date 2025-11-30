package com.finalproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
public class Controller {

    private final List<String> messages = List.of(
            "Keep going. You’re closer than you think.",
            "Believe in yourself. Progress takes time.",
            "Every step forward counts.",
            "Stay steady, stay focused.",
            "You’re doing great. Keep it up!"
    );

    @GetMapping("/motivation")
    public String getMotivation() {
        Random random = new Random();
        return messages.get(random.nextInt(messages.size()));
    }

    @GetMapping("/messages")
    public List<String> getMessages() {
        return messages;
    }

}