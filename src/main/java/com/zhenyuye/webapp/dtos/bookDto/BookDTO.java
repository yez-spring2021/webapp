package com.zhenyuye.webapp.dtos.bookDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class BookDTO {
    @NotNull(message = "title is required")
    private String title;
    @NotNull(message = "author is required")
    private String author;
    @NotNull(message = "isbn is required")
    private String isbn;
    @NotNull(message = "published_date is required")
    @JsonProperty("published_date")
    private String publishDate;
}
