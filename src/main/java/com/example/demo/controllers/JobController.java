package com.example.demo.controllers;
import com.example.demo.dtos.*;
import com.example.demo.services.JobService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public JobCreatedResponse createJob(
            @RequestBody @Valid CreateJobRequest request
    ) {

        return new JobCreatedResponse(
                jobService.createJob(request.urls())
        );
    }

    @GetMapping("/{jobId}")
    public JobResponse getJob(
            @PathVariable UUID jobId
    ) {
        return jobService.getJob(jobId);
    }
    @GetMapping("/{jobId}/tasks")
    public List<TaskResponse> getTasks(
            @PathVariable UUID jobId
    ) {
        return jobService.getTasks(jobId);
    }
}
