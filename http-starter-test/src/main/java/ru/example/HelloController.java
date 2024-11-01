package ru.example;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return String.format("Hello! %s!", name);
    }

    @PostMapping("/hello/{name}")
    public String postHello(@PathVariable String name) {
        return String.format("Hello! %s!", name);
    }
}
