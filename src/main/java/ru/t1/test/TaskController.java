package ru.t1.test;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello")
public class TaskController {

    @GetMapping
    public String hello(@RequestParam String name) {
        return String.format("Hello! %s!", name);
    }

    @PostMapping("/tasks")
    public String postHello(@PathVariable String name) {
        return String.format("Hello! %s!", name);
    }
}
