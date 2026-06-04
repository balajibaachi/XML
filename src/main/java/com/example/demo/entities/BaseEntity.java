package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    public void prePersist() {

        id = UUID.randomUUID();

        createdAt = Instant.now();

        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {

        updatedAt = Instant.now();
    }
}