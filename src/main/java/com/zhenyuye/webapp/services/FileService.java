package com.zhenyuye.webapp.services;

import com.zhenyuye.webapp.dtos.bookDto.FileData;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    FileData uploadImage(UUID bookId, MultipartFile image, String auth);
    List<FileData> getImages(String bookId);
    void deleteImage(UUID bookId, UUID fileId, String auth);
    boolean hasFile(String filename);
}
