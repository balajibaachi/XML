package com.example.demo.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;

@Service
@Slf4j
public class XmlFetcherService {

    private final HttpClient client =
            HttpClient.newBuilder()
                    .connectTimeout(
                            Duration.ofSeconds(10))
                    .build();

    @Retryable(
            retryFor = {
                    IOException.class,
                    InterruptedException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2
            )
    )
    public String fetch(String url)
            throws IOException,
            InterruptedException {

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() == 404) {

            throw new RuntimeException(
                    "404_NOT_FOUND"
            );
        }

        if (response.statusCode() >= 400) {

            throw new IOException(
                    "HTTP_ERROR "
                            + response.statusCode()
            );
        }

        return response.body();
    }
}
