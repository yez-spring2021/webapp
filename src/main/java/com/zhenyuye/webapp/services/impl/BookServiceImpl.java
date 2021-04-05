package com.zhenyuye.webapp.services.impl;

import com.alibaba.fastjson.JSON;
import com.timgroup.statsd.StatsDClient;
import com.zhenyuye.webapp.dtos.bookDto.BookDTO;
import com.zhenyuye.webapp.dtos.bookDto.BookData;
import com.zhenyuye.webapp.dtos.bookDto.FileData;
import com.zhenyuye.webapp.dtos.snsMsgDto.BookCreateMessageDTO;
import com.zhenyuye.webapp.dtos.snsMsgDto.BookDeleteMessageDTO;
import com.zhenyuye.webapp.exceptions.ValidationException;
import com.zhenyuye.webapp.model.Book;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.BookRepository;
import com.zhenyuye.webapp.services.BookService;
import com.zhenyuye.webapp.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private static final String TIMER_POSTFIX = "_timer";
    private static final String DB_BOOKS_QUERY_INSERT = "db.books.createBook";
    private static final String DB_BOOKS_QUERY_GET_ALL = "db.books.findAll";
    private static final String DB_BOOKS_QUERY_GET_ONE = "db.books.getOne";
    private static final String DB_BOOKS_QUERY_EXIST_ISBN = "db.books.existsBookByIsbn";
    private static final String DB_BOOKS_QUERY_DELETE = "db.books.removeBook";
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StatsDClient statsDClient;
    @Autowired
    private FileService fileService;
    @Autowired
    private AWSSNSService awssnsService;

    @Value("${domain.name}")
    private String domainName;

    @Override
    public BookData createBook(BookDTO bookDTO, String auth) throws MalformedURLException {
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
        String bookId = book.getId().toString();
        URL url = UriComponentsBuilder.fromHttpUrl(String.join("/", domainName, "books", bookId)).build().toUri().toURL();
        BookCreateMessageDTO message = BookCreateMessageDTO.builder()
                .bookId(bookId)
                .bookName(book.getTitle())
                .email(user.getEmail())
                .link(url.toString())
                .type("CREATE")
                .build();
        awssnsService.publishMsgToTopic(JSON.toJSONString(message));
        return generateBookData(book);
    }

    @Transactional
    public Book createBook(Book book) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Book savedBook = bookRepository.save(book);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_BOOKS_QUERY_INSERT + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return savedBook;
    }


    @Override
    public BookData getBook(UUID bookId) {
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Book book = bookRepository.getOne(bookId);
            stopWatch.stop();
            statsDClient.recordExecutionTime(DB_BOOKS_QUERY_GET_ONE + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
            return generateBookData(book);
        } catch (Exception e) {
            throw new ValidationException("Book does not exist.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void removeBook(UUID bookId, String auth) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Book book = bookRepository.findById(bookId).orElse(null);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_BOOKS_QUERY_GET_ONE + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
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
        stopWatch.start();
        bookRepository.delete(book);
        stopWatch.stop();
        BookDeleteMessageDTO message = BookDeleteMessageDTO.builder()
                .bookName(book.getTitle())
                .bookId(bookId.toString())
                .email(user.getEmail())
                .type("DELETE")
                .build();
        awssnsService.publishMsgToTopic(JSON.toJSONString(message));
        statsDClient.recordExecutionTime(DB_BOOKS_QUERY_DELETE + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
    }

    @Override
    public List<BookData> getBooks() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Book> books = bookRepository.findAll();
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_BOOKS_QUERY_GET_ALL + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return books.stream().map(this::generateBookData).collect(Collectors.toList());
    }

    @Override
    public boolean isbnCheck(String isbn) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean exist = bookRepository.existsBookByIsbn(isbn);
        stopWatch.stop();
        statsDClient.recordExecutionTime(DB_BOOKS_QUERY_EXIST_ISBN + TIMER_POSTFIX, stopWatch.getLastTaskTimeMillis());
        return exist;
    }

    private BookData generateBookData(Book book) {
        List<FileData> fileDataList = fileService.getImages(book.getId().toString());
        return BookData.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publishDate(book.getPublishedDate())
                .title(book.getTitle())
                .userId(book.getUserId())
                .bookCreated(book.getBookCreated())
                .bookImages(fileDataList)
                .build();
    }
}
