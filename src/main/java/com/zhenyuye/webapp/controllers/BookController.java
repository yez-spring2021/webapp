package com.zhenyuye.webapp.controllers;

import com.timgroup.statsd.StatsDClient;
import com.zhenyuye.webapp.annotation.ValidImage;
import com.zhenyuye.webapp.dtos.bookDto.BookDTO;
import com.zhenyuye.webapp.dtos.bookDto.BookData;
import com.zhenyuye.webapp.dtos.bookDto.FileData;
import com.zhenyuye.webapp.dtos.bookDto.ImageDTO;
import com.zhenyuye.webapp.services.BookService;
import com.zhenyuye.webapp.services.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

import static com.zhenyuye.webapp.utils.ValidationUtil.verifyInput;

@RestController
@Slf4j
public class BookController {
    private static final String ENDPOINT_PREFIX = "books.";
    private static final String GET_BOOK_ID_ENDPOINT = ENDPOINT_PREFIX + "id.http.get";
    private static final String GET_ALL_BOOKS_ENDPOINT = ENDPOINT_PREFIX + "http.get";
    private static final String CREATE_BOOK_ENDPOINT = ENDPOINT_PREFIX + "http.post";
    private static final String DELETE_BOOK_ID_ENDPOINT = ENDPOINT_PREFIX + "id.http.delete";
    private static final String UPLOAD_IMAGE_ENDPOINT = "id.image.http.post";
    private static final String DELETE_IMAGE_ENDPOINT = "id.image.imageId.http.delete";
    private static final String COUNTER_POSTFIX = "_counter";
    private static final String TIMER_POSTFIX = "_timer";
    private static final String LOG_PREFIX = "BookController";

    @Autowired
    private BookService bookService;

    @Autowired
    private FileService fileService;

    @Autowired
    private StatsDClient statsDClient;

    @GetMapping("/books/{id}")
    @ResponseBody
    public BookData getBookById(@PathVariable("id") UUID bookId) {
        log.info(LOG_PREFIX+".getBookById.{}", bookId.toString());
        statsDClient.incrementCounter(GET_BOOK_ID_ENDPOINT + COUNTER_POSTFIX);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        BookData bookData = bookService.getBook(bookId);
        stopWatch.stop();
        statsDClient.recordExecutionTime(GET_BOOK_ID_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return bookData;
    }

    @GetMapping("/books")
    @ResponseBody
    public List<BookData> getBooks() {
        log.info(LOG_PREFIX+".getBooks");
        statsDClient.incrementCounter(GET_ALL_BOOKS_ENDPOINT + COUNTER_POSTFIX);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<BookData> bookData = bookService.getBooks();
        stopWatch.stop();
        statsDClient.recordExecutionTime(GET_ALL_BOOKS_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return bookData;
    }

    @PostMapping("/books")
    public ResponseEntity<BookData> createBook(@RequestHeader("authorization") String auth, @Valid @RequestBody BookDTO bookDTO, BindingResult result) throws MalformedURLException {
        log.info(LOG_PREFIX+".createBook");
        statsDClient.incrementCounter(CREATE_BOOK_ENDPOINT + COUNTER_POSTFIX);
        verifyInput(result);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        BookData bookData = bookService.createBook(bookDTO, auth);
        stopWatch.stop();
        statsDClient.recordExecutionTime(CREATE_BOOK_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return new ResponseEntity<>(bookData, HttpStatus.CREATED);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Object> removeBook(@RequestHeader("authorization") String auth, @PathVariable("id") UUID bookId) {
        log.info(LOG_PREFIX+".removeBook.{}", bookId.toString());
        statsDClient.incrementCounter(DELETE_BOOK_ID_ENDPOINT + COUNTER_POSTFIX);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        bookService.removeBook(bookId, auth);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DELETE_BOOK_ID_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/books/{id}/image", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileData> uploadImage(@RequestHeader("authorization") String auth, @PathVariable("id") UUID bookId, @ValidImage ImageDTO imageDTO, BindingResult result) {
        log.info(LOG_PREFIX+".uploadImage.{}", bookId.toString());
        statsDClient.incrementCounter(UPLOAD_IMAGE_ENDPOINT + COUNTER_POSTFIX);
        verifyInput(result);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        FileData fileData = fileService.uploadImage(bookId, imageDTO.getImage(), auth);
        stopWatch.stop();
        statsDClient.recordExecutionTime(UPLOAD_IMAGE_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return new ResponseEntity<>(fileData, HttpStatus.CREATED);
    }

    @DeleteMapping("/books/{book_id}/image/{image_id}")
    public ResponseEntity<Object> removeImage(@RequestHeader("authorization") String auth, @PathVariable("book_id") UUID bookId, @PathVariable("image_id") UUID fileId) {
        log.info(LOG_PREFIX+".removeImage.{}.{}", bookId.toString(), fileId.toString());
        statsDClient.incrementCounter(DELETE_IMAGE_ENDPOINT + COUNTER_POSTFIX);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        fileService.deleteImage(bookId, fileId, auth);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DELETE_IMAGE_ENDPOINT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
