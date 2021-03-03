package com.zhenyuye.webapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "file_metadata")
public class FileMetaData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type="uuid-char")
    @Column(name = "metadata_id",columnDefinition = "varchar(256)", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "content_type",columnDefinition = "varchar(128)")
    private String contentType;
    @Column(name = "content_length")
    private long contentLength;
    @Column(name = "last_modified")
    private Date lastModified;
    @Column(name = "version_id")
    private String versionId;
    @Column(name = "file_id", nullable = false)
    private String fileId;
    @Column(name = "s3_object_name", nullable = false)
    private String s3ObjectName;
}
