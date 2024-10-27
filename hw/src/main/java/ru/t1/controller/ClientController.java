package ru.t1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.service.ClientService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @GetMapping(value = "/{clients}/parse")
    public void parseSource(@PathVariable(name = "clients") Long numberOfclients) {
        clientService.sendClients(numberOfclients);
    }
}
