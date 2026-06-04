package com.example.demo.entities;

import com.example.demo.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    private String url;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Integer recordsExtracted;

    @Column(columnDefinition = "TEXT")
    private String error;
}