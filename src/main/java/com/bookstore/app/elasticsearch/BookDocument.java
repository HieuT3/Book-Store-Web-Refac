package com.bookstore.app.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "books")
public class BookDocument {
    @Id
    String bookId;

    @MultiField(
            mainField = @Field(type = FieldType.Text),
            otherFields = {
                    @InnerField(type = FieldType.Keyword, suffix = "keyword")
            }
    )
    String title;

    @Field(type = FieldType.Text)
    String description;

    @Field(type = FieldType.Text)
    String smallImageUrl;

    @Field(type = FieldType.Double)
    double price;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate publishedDate;

    @Field(type = FieldType.Keyword)
    List<String> authors;

    @Field(type = FieldType.Keyword)
    List<String> categories;
}
