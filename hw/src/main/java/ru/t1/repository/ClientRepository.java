package ru.t1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.entity.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Override
    Optional<Client> findById(Long aLong);
}