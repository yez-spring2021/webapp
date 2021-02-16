package com.zhenyuye.webapp.services.impl;

import com.zhenyuye.webapp.dtos.bookDto.BookDTO;
import com.zhenyuye.webapp.dtos.bookDto.BookData;
import com.zhenyuye.webapp.exceptions.ValidationException;
import com.zhenyuye.webapp.model.Book;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.BookRepository;
import com.zhenyuye.webapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;

    @Override
    public BookData createBook(BookDTO bookDTO, String auth) {
        User user = userService.getUser(auth);
        if (user == null) {
            throw new ValidationException("Cannot validate current user");
        }
        Book book = Book.builder()
                .author(bookDTO.getAuthor())
                .publishedDate(bookDTO.getPublishDate())
                .userId(user.getId().toString())
                .title(bookDTO.getTitle())
                .isbn(bookDTO.getIsbn())
                .build();
        book = createBook(book);
        return generateBookData(book);
    }

    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }


    @Override
    public BookData getBook(UUID bookId) {
        try {
            Book book = bookRepository.getOne(bookId);
            return generateBookData(book);
        } catch (Exception e) {
            throw new ValidationException("Book does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void removeBook(UUID bookId, String auth) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) {
            throw new ValidationException("Book does not exist.", HttpStatus.NOT_FOUND);
        }
        User user = userService.getUser(auth);
        if (user == null) {
            throw new ValidationException("Cannot validate current user");
        }
        if (!book.getUserId().equals(user.getId().toString())) {
            throw new ValidationException("User don't have access to remove.", HttpStatus.UNAUTHORIZED);
        }
        bookRepository.delete(book);
    }

    @Override
    public List<BookData> getBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(this::generateBookData).collect(Collectors.toList());
    }

    @Override
    public boolean isbnCheck(String isbn) {
        return bookRepository.existsBookByIsbn(isbn);
    }

    private BookData generateBookData(Book book) {
        return BookData.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publishDate(book.getPublishedDate())
                .title(book.getTitle())
                .userId(book.getUserId())
                .bookCreated(book.getBookCreated())
                .build();
    }
}
