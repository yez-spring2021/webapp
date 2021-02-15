package com.zhenyuye.webapp.dtos.bookDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
public class BookData {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    @JsonProperty("published_date")
    private String publishDate;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("book_created")
    private Date bookCreated;
}
