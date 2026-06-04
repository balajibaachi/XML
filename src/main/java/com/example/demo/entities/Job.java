package com.example.demo.entities;

import com.example.demo.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private Integer totalUrls;

    private Integer completed;

    private Integer failed;
}