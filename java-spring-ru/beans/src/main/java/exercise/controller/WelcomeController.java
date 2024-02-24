package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    private final Daytime daytime;

    public WelcomeController(Daytime daytime) {
        this.daytime = daytime;
    }

    @GetMapping
    public String hello() {
        return "It is %s now! Welcome to Spring!".formatted(daytime.getName());
    }
}