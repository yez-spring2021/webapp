package com.zhenyuye.webapp.repositories;

import com.zhenyuye.webapp.model.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileMetaDataRepository extends JpaRepository<FileMetaData, UUID> {
    void deleteFileMetaDataByS3ObjectName(String s3ObjectName);
}
