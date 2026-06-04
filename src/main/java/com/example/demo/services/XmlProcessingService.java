package com.example.demo.services;


import com.example.demo.dtos.ParsedArticle;
import com.example.demo.entities.*;
import com.example.demo.enums.TaskStatus;
import com.example.demo.repositories.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class XmlProcessingService {

    private final TaskRepository taskRepository;

    private final ArticleRepository articleRepository;

    private final XmlFetcherService fetcher;

    private final XmlParserService parser;
    private final JobService jobService;

    @Async("xmlExecutor")
    public void processTask(UUID taskId) {

        Task task =
                taskRepository.findById(taskId)
                        .orElseThrow();

        task.setStatus(
                TaskStatus.IN_PROGRESS
        );

        taskRepository.save(task);

        try {

            log.info(
                    "fetch_started taskId={} url={}",
                    task.getId(),
                    task.getUrl()
            );

            String xml =
                    fetcher.fetch(
                            task.getUrl()
                    );

            List<ParsedArticle> articles =
                    parser.parse(xml);

            saveArticles(
                    task,
                    articles
            );

            task.setStatus(
                    TaskStatus.COMPLETED
            );

            task.setRecordsExtracted(
                    articles.size()
            );

            taskRepository.save(task);

            log.info(
                    "records_persisted taskId={} count={}",
                    task.getId(),
                    articles.size()
            );
            task.setStatus(TaskStatus.COMPLETED);
            task.setRecordsExtracted(articles.size());

            taskRepository.save(task);

            jobService.updateJobStatus(
                    task.getJob().getId()
            );

        } catch (Exception ex) {

            task.setStatus(
                    TaskStatus.FAILED
            );

            task.setError(
                    ex.getMessage()
            );

            taskRepository.save(task);

            log.error(
                    "task_failed taskId={} error={}",
                    task.getId(),
                    ex.getMessage()
            );
            task.setStatus(TaskStatus.FAILED);
            task.setError(ex.getMessage());

            taskRepository.save(task);

            jobService.updateJobStatus(
                    task.getJob().getId()
            );
        }
    }

    private void saveArticles(
            Task task,
            List<ParsedArticle> articles
    ) {

        articles.forEach(a -> {

            Article article =
                    Article.builder()
                            .task(task)
                            .title(a.title())
                            .link(a.link())
                            .author(a.author())
                            .publishedDate(
                                    a.publishedDate())
                            .summary(
                                    a.summary())
                            .build();

            articleRepository.save(article);
        });
    }
}