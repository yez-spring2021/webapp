package com.zhenyuye.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "files")
public class File {
    @Id
    @Type(type="uuid-char")
    @Column(name = "file_id",columnDefinition = "varchar(256)", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "file_name",unique = true, nullable = false)
    private String fileName;
    @Column(name = "s3_object_name",columnDefinition = "varchar(512)",updatable = false, nullable = false)
    private String s3ObjectName;
    @Column(name = "created_date",updatable = false, nullable = false)
    @CreationTimestamp
    private Date createdDate;
    @Column(name = "user_id", updatable = false, nullable = false)
    private String userId;
    @Column(name= "book_id", updatable = false, nullable = false)
    private String bookId;
}
