package com.zhenyuye.webapp.services.impl;

import com.timgroup.statsd.StatsDClient;
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
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {
    private static final String TIMER_POSTFIX = "_timer";
    private static final String DB_BOOKS_FIND_BY_ID = "db.books.findById";
    private static final String DB_FILES_INSERT = "db.files.insert";
    private static final String DB_FILES_FIND_FILES_BY_BOOK_ID = "db.files.findFilesByBookId";
    private static final String DB_FILES_DELETE = "db.files.delete";
    private static final String DB_FILES_EXIST_BY_FILENAME = "db.files.existsByFileName";
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private S3FileService s3FileService;
    @Autowired
    private StatsDClient statsDClient;

    @Override
    public FileData uploadImage(UUID bookId, MultipartFile image, String auth) {
        User user = userService.getUser(auth);
        if (user == null) {
            throw new ValidationException("Cannot validate current user");
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Book book = bookRepository.findById(bookId).orElse(null);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_BOOKS_FIND_BY_ID + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        if (book == null) {
            throw new ValidationException("Book does not exist", HttpStatus.NOT_FOUND);
        }
        if (!book.getUserId().equals(user.getId().toString())) {
            throw new ValidationException("Only the owner of this book can upload", HttpStatus.FORBIDDEN);
        }
        File file = s3FileService.uploadImage(image, book, image.getOriginalFilename());
        return saveFile(file);
    }

    @Transactional
    public FileData saveFile(File file) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File savedFile = fileRepository.save(file);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_FILES_INSERT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<File> files = fileRepository.findFilesByBookId(bookId);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_FILES_FIND_FILES_BY_BOOK_ID + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
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
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        File file = fileRepository.findById(fileId).orElse(null);
        if (file == null) {
            stopWatch.stop();
            throw new ValidationException("Image does not exist.", HttpStatus.NOT_FOUND);
        }
        if (!user.getId().toString().equals(file.getUserId())) {
            stopWatch.stop();
            throw new ValidationException("User not authorized.", HttpStatus.UNAUTHORIZED);
        }
        fileRepository.delete(file);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_FILES_DELETE + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        s3FileService.deleteImage(file);
    }

    @Override
    public boolean hasFile(String filename) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean exist = fileRepository.existsByFileName(filename);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_FILES_EXIST_BY_FILENAME + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return exist;
    }
}
