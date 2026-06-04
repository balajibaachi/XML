package com.example.demo.repositories;


import com.example.demo.entities.Task;
import com.example.demo.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository
        extends JpaRepository<Task, UUID> {

    List<Task> findByJobId(UUID jobId);
    long countByJobIdAndStatus(UUID jobId, TaskStatus status);
}
