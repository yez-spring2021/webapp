package com.zhenyuye.webapp.controllers;

import com.zhenyuye.webapp.dtos.bookDto.BookDTO;
import com.zhenyuye.webapp.dtos.bookDto.BookData;
import com.zhenyuye.webapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static com.zhenyuye.webapp.utils.ValidationUtil.verifyInput;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/books/{id}")
    @ResponseBody
    public BookData getBookById(@PathVariable("id") UUID bookId) {
        return bookService.getBook(bookId);
    }

    @GetMapping("/books")
    @ResponseBody
    public List<BookData> getBooks() {
        return bookService.getBooks();
    }

    @PostMapping("/books")
    public ResponseEntity<BookData> createBook(@RequestHeader("authorization") String auth, @Valid @RequestBody BookDTO bookDTO,BindingResult result) {
        verifyInput(result);
        BookData bookData = bookService.createBook(bookDTO, auth);
        return new ResponseEntity<>(bookData, HttpStatus.CREATED);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Object> removeBook(@RequestHeader("authorization") String auth, @PathVariable("id") UUID bookId) {
        bookService.removeBook(bookId, auth);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
