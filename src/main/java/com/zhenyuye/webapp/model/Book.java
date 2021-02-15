package com.zhenyuye.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type="uuid-char")
    @Column(name = "book_id",columnDefinition = "varchar(256)", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "published_date", nullable = false)
    private String publishedDate;
    @Column(name = "isbn", nullable = false)
    private String isbn;
    @Column(name = "book_created", updatable = false, nullable = false)
    @CreationTimestamp
    private Date bookCreated;
    @Column(name = "user_id", updatable = false, nullable = false)
    private String userId;
}
