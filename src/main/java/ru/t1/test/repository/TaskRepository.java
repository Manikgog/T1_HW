package ru.t1.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.test.entity.Task;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findById(int id);
}
