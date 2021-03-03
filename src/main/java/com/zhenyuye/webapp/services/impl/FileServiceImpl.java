package com.zhenyuye.webapp.services.impl;

import com.zhenyuye.webapp.dtos.bookDto.FileData;
import com.zhenyuye.webapp.exceptions.ValidationException;
import com.zhenyuye.webapp.model.Book;
import com.zhenyuye.webapp.model.File;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.BookRepository;
import com.zhenyuye.webapp.repositories.FileRepository;
import com.zhenyuye.webapp.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private S3FileService s3FileService;
    @Override
    public FileData uploadImage(UUID bookId, MultipartFile image, String auth) {
        User user = userService.getUser(auth);
        if (user == null) {
            throw new ValidationException("Cannot validate current user");
        }
        Book book = bookRepository.findById(bookId).orElse(null);
        if(book==null){
            throw new ValidationException("Book does not exist", HttpStatus.NOT_FOUND);
        }
        if(!book.getUserId().equals(user.getId().toString())) {
            throw new ValidationException("Only the owner of this book can upload", HttpStatus.FORBIDDEN);
        }
        File file = s3FileService.uploadImage(image, book, image.getOriginalFilename());
        return saveFile(file);
    }

    @Transactional
    public FileData saveFile(File file) {
        File savedFile = fileRepository.save(file);
        return FileData.builder()
                .fileId(savedFile.getId())
                .fileName(savedFile.getFileName())
                .s3ObjectName(savedFile.getS3ObjectName())
                .userId(savedFile.getUserId())
                .createdDate(savedFile.getCreatedDate())
                .build();
    }

    @Override
    public List<FileData> getImages(String bookId) {
        List<File> files = fileRepository.findFilesByBookId(bookId);
        return files.stream().map(file -> FileData.builder()
                .fileId(file.getId())
                .fileName(file.getFileName())
                .s3ObjectName(file.getS3ObjectName())
                .userId(file.getUserId())
                .createdDate(file.getCreatedDate())
                .build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteImage(UUID bookId, UUID fileId, String auth) {
        User user = userService.getUser(auth);
        if (user == null) {
            throw new ValidationException("Cannot validate current user");
        }
        File file = fileRepository.findById(fileId).orElse(null);
        if(file==null) {
            throw new ValidationException("Image does not exist.", HttpStatus.NOT_FOUND);
        }
        if(!user.getId().toString().equals(file.getUserId())) {
            throw new ValidationException("User not authorized.", HttpStatus.UNAUTHORIZED);
        }
        fileRepository.delete(file);
        s3FileService.deleteImage(file);
    }
    @Override
    public boolean hasFile(String filename){
        return fileRepository.existsByFileName(filename);
    }
}
