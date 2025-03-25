package com.bookstore.app.service.impl;

import co.elastic.clients.json.JsonData;
import com.bookstore.app.elasticsearch.BookDocument;
import com.bookstore.app.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    ElasticsearchOperations elasticsearchOperations;

    private Page<BookDocument> getResults(NativeQuery query) {
        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query, BookDocument.class);
        SearchPage<BookDocument> searchPage = SearchHitSupport.searchPageFor(searchHits, query.getPageable());
        return searchPage.map(SearchHit::getContent);
    }

    public Page<BookDocument> search(String title,
                                     String author,
                                     String category,
                                     int pageNumber,
                                     int pageSize,
                                     String order) {
        Sort sort = order.equalsIgnoreCase("asc") ?
                Sort.by(Sort.Direction.ASC, "price") :
                order.equalsIgnoreCase("desc") ?
                Sort.by(Sort.Direction.DESC, "price") :
                Sort.unsorted();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        boolean hasTitle = !title.isEmpty();
        boolean hasAuthor = !author.isEmpty();
        boolean hasCategory = !category.isEmpty();

        if (hasTitle && hasAuthor && hasCategory) {
            return searchBooksByTitleAndAuthorAndCategory(title, author, category, pageable);
        } else if (hasTitle && hasAuthor) {
            return searchBooksByTitleAndAuthor(title, author, pageable);
        } else if (hasTitle && hasCategory) {
            return searchBooksByTitleAndCategory(title, category, pageable);
        } else if (hasAuthor && hasCategory) {
            return searchBooksByAuthorAndCategory(author, category, pageable);
        } else if (hasTitle) {
            return searchBooksByTitle(title, pageable);
        } else if (hasAuthor) {
            return filterBooksByAuthor(author, pageable);
        } else if (hasCategory) {
            return filterBooksByCategory(category, pageable);
        } else {
            return Page.empty();
        }
    }

    @Override
    public Page<BookDocument> searchBooksByTitle(String title, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m.field("title").query(title))
                )
                .withPageable(pageable)
                .build();
        return getResults(query);
    }

    @Override
    public Page<BookDocument> searchBooksByTitleAndAuthor(String title, String author, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(ms -> ms.match(m -> m.field("title").query(title)))
                                .must(ms -> ms.term(t -> t.field("authors").value(author)))
                        )
                )
                .withPageable(pageable)
                .build();
        return getResults(query);
    }

    @Override
    public Page<BookDocument> searchBooksByTitleAndCategory(String title, String category, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(ms -> ms.match(m -> m.field("title").query(title)))
                                .must(ms -> ms.term(t -> t.field("categories").value(category)))
                        )
                )
                .withPageable(pageable)
                .build();
        return getResults(query);
    }

    @Override
    public Page<BookDocument> searchBooksByAuthorAndCategory(String author, String category, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(ms -> ms.term(t -> t.field("authors").value(author)))
                                .must(ms -> ms.term(t -> t.field("categories").value(category)))
                        )
                )
                .withPageable(pageable)
                .build();
        return getResults(query);
    }

    @Override
    public Page<BookDocument> searchBooksByTitleAndAuthorAndCategory(String title, String author, String category, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .must(ms -> ms.match(m -> m.field("title").query(title)))
                                .must(ms -> ms.term(t -> t.field("authors").value(author)))
                                .must(ms -> ms.term(t -> t.field("categories").value(category)))
                        )
                )
                .withPageable(pageable)
                .build();
        return getResults(query);
    }

    @Override
    public Page<BookDocument> filterBooksByAuthor(String author, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .filter(f -> f
                                        .term(t -> t.field("authors").value(author))
                                )
                        )
                )
                .withPageable(pageable)
                .build();
        return getResults(query);
    }

    @Override
    public Page<BookDocument> filterBooksByCategory(String category, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                .filter(f -> f
                                        .term(t -> t.field("categories").value(category))
                                )
                        )
                )
                .withPageable(pageable)
                .build();
        return getResults(query);
    }

    @Override
    public Page<BookDocument> rangeBooksByPrice(double lte, double gte, Pageable pageable) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .range(r -> r
                                .field("price")
                                .lte(JsonData.of(lte))
                                .gte(JsonData.of(gte))
                        )
                )
                .withPageable(pageable)
                .build();
        return getResults(query);
    }
}
