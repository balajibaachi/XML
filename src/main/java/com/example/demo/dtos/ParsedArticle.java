package com.example.demo.dtos;

import java.time.Instant;

public record ParsedArticle(
        String title,
        String link,
        String author,
        Instant publishedDate,
        String summary
) {
}
