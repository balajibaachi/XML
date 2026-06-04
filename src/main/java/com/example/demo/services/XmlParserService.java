package com.example.demo.services;


import com.example.demo.dtos.ParsedArticle;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;

import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.time.Instant;
import java.util.*;

@Service
public class XmlParserService {

    public List<ParsedArticle> parse(
            String xml
    ) {

        try {

            SyndFeed feed =
                    new SyndFeedInput()
                            .build(
                                    new XmlReader(
                                            new StringReader(xml)
                                    )
                            );

            List<ParsedArticle> articles =
                    new ArrayList<>();

            for (SyndEntry entry :
                    feed.getEntries()) {

                articles.add(
                        new ParsedArticle(
                                entry.getTitle(),
                                entry.getLink(),
                                entry.getAuthor(),
                                entry.getPublishedDate() != null
                                        ? entry.getPublishedDate()
                                        .toInstant()
                                        : null,
                                entry.getDescription() != null
                                        ? entry.getDescription()
                                        .getValue()
                                        : null
                        )
                );
            }

            return articles;

        } catch (Exception ex) {

            throw new RuntimeException(
                    "MALFORMED_XML",
                    ex
            );
        }
    }
}
