package com.example.demo.dtos;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateJobRequest(
        @NotEmpty
        List<String> urls
) {}