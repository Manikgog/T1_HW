package ru.t1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.dto.ClientDto;
import ru.t1.entity.Client;
import ru.t1.kafka.KafkaClientProducer;
import ru.t1.repository.ClientRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    @Value("${t1.kafka.topic.client_registration}")
    private String topic;

    @Value("${t1.kafka.topic.client_id_registered}")
    private String clientRegistrationTopic;

    private final KafkaClientProducer kafkaClientProducer;

    private final ClientRepository repository;

    private final ClientsGenerator clientsGenerator;

    public void registerClients(List<Client> clients) {
        log.info("Registering clients... {}", clients);
        repository.saveAll(clients)
                .forEach(c -> kafkaClientProducer.send(clientRegistrationTopic, c));
    }

    public void sendClients(Long numberOfClients) {
        log.info("Sending clients...");
        List<ClientDto> clientDtos = clientsGenerator.generate(numberOfClients);
        clientDtos.forEach(dto -> kafkaClientProducer.sendTo(topic, dto));
    }

}
