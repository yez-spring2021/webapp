package com.zhenyuye.webapp.dtos.bookDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
public class FileData {
    @JsonProperty("file_id")
    private UUID fileId;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("s3_object_name")
    private String s3ObjectName;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("created_date")
    private Date createdDate;
}
