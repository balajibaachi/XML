package com.example.demo.repositories;


import com.example.demo.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository
        extends JpaRepository<Job, UUID> {
}
