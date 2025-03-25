package com.bookstore.app.elasticsearch;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, String> {
    @Query(value = "{" +
            "\"term\": {\"title\": \"?0\"}" +
    "}")
    List<BookDocument> findByTitle(String title);
}
