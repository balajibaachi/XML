package com.example.demo.services;



import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.enums.JobStatus;
import com.example.demo.enums.TaskStatus;
import com.example.demo.exception.JobNotFoundException;
import com.example.demo.repositories.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final TaskRepository taskRepository;
    private final XmlProcessingService xmlProcessingService;
    @Transactional
    public UUID createJob(List<String> urls) {

        Job job = Job.builder()
                .status(JobStatus.RUNNING)
                .totalUrls(urls.size())
                .completed(0)
                .failed(0)
                .build();

        jobRepository.save(job);

        for (String url : urls) {

            Task task = Task.builder()
                    .job(job)
                    .url(url)
                    .status(TaskStatus.PENDING)
                    .recordsExtracted(0)
                    .build();

            taskRepository.save(task);

            /*
             * Fire async processing
             */
            xmlProcessingService.processTask(
                    task.getId()
            );
        }

        return job.getId();
    }

    public JobResponse getJob(UUID jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new JobNotFoundException(
                                "Job not found: " + jobId));

        long completed =
                taskRepository.countByJobIdAndStatus(
                        jobId,
                        TaskStatus.COMPLETED
                );

        long failed =
                taskRepository.countByJobIdAndStatus(
                        jobId,
                        TaskStatus.FAILED
                );

        long pending =
                taskRepository.countByJobIdAndStatus(
                        jobId,
                        TaskStatus.PENDING
                );

        long inProgress =
                taskRepository.countByJobIdAndStatus(
                        jobId,
                        TaskStatus.IN_PROGRESS
                );

        return new JobResponse(
                job.getId(),
                job.getStatus().name(),
                job.getTotalUrls(),
                completed,
                failed,
                pending,
                inProgress
        );
    }

    public List<TaskResponse> getTasks(UUID jobId) {

        if (!jobRepository.existsById(jobId)) {
            throw new JobNotFoundException(
                    "Job not found: " + jobId
            );
        }

        return taskRepository.findByJobId(jobId)
                .stream()
                .map(task -> new TaskResponse(
                        task.getUrl(),
                        task.getStatus().name(),
                        task.getRecordsExtracted(),
                        task.getError()
                ))
                .toList();
    }

    @Transactional
    public void updateJobStatus(UUID jobId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow();

        long completed =
                taskRepository.countByJobIdAndStatus(
                        jobId,
                        TaskStatus.COMPLETED
                );

        long failed =
                taskRepository.countByJobIdAndStatus(
                        jobId,
                        TaskStatus.FAILED
                );

        if ((completed + failed)
                == job.getTotalUrls()) {

            if (failed > 0) {
                job.setStatus(JobStatus.FAILED);
            } else {
                job.setStatus(JobStatus.COMPLETED);
            }

            job.setCompleted((int) completed);
            job.setFailed((int) failed);

            jobRepository.save(job);
        }
    }
}