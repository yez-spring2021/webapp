package com.zhenyuye.webapp.services.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.zhenyuye.webapp.model.Book;
import com.zhenyuye.webapp.model.File;
import com.zhenyuye.webapp.model.FileMetaData;
import com.zhenyuye.webapp.repositories.FileMetaDataRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class S3FileService {
    @Value("${amazon.s3.bucketName}")
    private String s3BucketName;

    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;
    @Autowired
    private AmazonS3 s3Client;

    public File uploadImage(MultipartFile image, Book book, String fileName) {
        UUID fileId = UUID.randomUUID();
        String s3ObjectName = String.join("/", book.getId().toString(), fileId.toString(), fileName);
        InputStream inputStream = null;
        ObjectMetadata metadata = new ObjectMetadata();
        try {
            inputStream = image.getInputStream();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());
            metadata.setLastModified(Date.from(Instant.now()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PutObjectResult result = s3Client.putObject(s3BucketName, s3ObjectName, inputStream, metadata);
        S3Object object = s3Client.getObject(s3BucketName, s3ObjectName);
        FileMetaData fileMetaData = new FileMetaData();
        BeanUtils.copyProperties(object.getObjectMetadata(), fileMetaData);
        fileMetaData.setFileId(fileId.toString());
        fileMetaData.setS3ObjectName(s3ObjectName);
        saveFileMetaData(fileMetaData);
        return File.builder()
                .bookId(book.getId().toString())
                .id(fileId)
                .userId(book.getUserId())
                .s3ObjectName(s3ObjectName)
                .fileName(fileName)
                .build();

    }
    @Transactional
    public void saveFileMetaData(FileMetaData fileMetaData) {
        fileMetaDataRepository.save(fileMetaData);
    }

    @Transactional
    public boolean deleteImage(File file) {
        String s3ObjectName = file.getS3ObjectName();
        s3Client.deleteObject(new DeleteObjectRequest(s3BucketName, s3ObjectName));
        fileMetaDataRepository.deleteFileMetaDataByS3ObjectName(s3ObjectName);
        return true;
    }
}
