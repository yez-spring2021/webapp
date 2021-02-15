package com.zhenyuye.webapp.services;

import com.zhenyuye.webapp.dtos.bookDto.BookDTO;
import com.zhenyuye.webapp.dtos.bookDto.BookData;
import com.zhenyuye.webapp.model.Book;
import com.zhenyuye.webapp.model.User;
import com.zhenyuye.webapp.repositories.BookRepository;
import com.zhenyuye.webapp.services.impl.BookServiceImpl;
import com.zhenyuye.webapp.services.impl.UserService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BookServiceTest {
    private static final String TEST_USERNAME = "test@test.com";
    private static final String TEST_PASSWORD = "passwordTest-1";
    private static final String TEST_FIRSTNAME = "test";
    private static final String TEST_LASTNAME = "test";
    private static final String AUTH = "Basic: auth";
    private static final UUID UU_ID = UUID.randomUUID();

    private static final BookDTO BOOK_DTO = BookDTO.builder().isbn("isbn").title("title").author("author").publishDate("date").build();
    private static final Book BOOK = Book.builder().id(UU_ID).isbn("isbn").title("title").author("author").publishedDate("date").userId(UU_ID.toString()).build();
    private static final User USER = User.builder().id(UU_ID).username(TEST_USERNAME).password(TEST_PASSWORD).firstName(TEST_FIRSTNAME).lastName(TEST_LASTNAME).build();

    @Mock
    private UserService userService;

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void createBookTest() {
        // Arrange
        when(userService.getUser(Mockito.anyString())).thenReturn(USER);
        when(bookRepository.save(Mockito.any(Book.class))).thenReturn(BOOK);

        BookData bookData = bookService.createBook(BOOK_DTO, AUTH);

        Assertions.assertEquals(BOOK.getAuthor(), bookData.getAuthor());
        Assertions.assertEquals(BOOK.getIsbn(), bookData.getIsbn());
        Assertions.assertEquals(BOOK.getTitle(), bookData.getTitle());
        Assertions.assertEquals(BOOK.getPublishedDate(), bookData.getPublishDate());
    }

    @Test
    public void getBookTest() {
        // Arrange
        when(bookRepository.getOne(UU_ID)).thenReturn(BOOK);

        // Act
        BookData bookData = bookService.getBook(UU_ID);

        // Assert
        Assertions.assertEquals(BOOK.getId().toString(), bookData.getId().toString());
    }

    @Test
    public void removeBookTest() {
        Optional<Book> optional = Optional.of(BOOK);
        when(bookRepository.findById(UU_ID)).thenReturn(optional);
        when(userService.getUser(Mockito.anyString())).thenReturn(USER);
        doNothing().when(bookRepository).delete(Mockito.any(Book.class));
        bookService.removeBook(UU_ID, AUTH);
        verify(bookRepository, times(1)).delete(Mockito.any(Book.class));
    }

    @Test
    public void getAllBooks() {
        List<Book> books = Collections.singletonList(BOOK);
        when(bookRepository.findAll()).thenReturn(books);
        List<BookData> result = bookService.getBooks();
        Assertions.assertEquals(books.size(), result.size());
    }
}
