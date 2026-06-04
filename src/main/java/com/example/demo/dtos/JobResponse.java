package com.example.demo.dtos;


import java.util.UUID;

public record JobResponse(
        UUID jobId,
        String status,
        Integer totalUrls,
        long completed,
        long failed,
        long pending,
        long inProgress
) {}
