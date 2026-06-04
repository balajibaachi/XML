package com.example.demo.dtos;

public record TaskResponse(
        String url,
        String status,
        Integer recordsExtracted,
        String error
) {}
