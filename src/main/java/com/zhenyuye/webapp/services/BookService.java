package com.zhenyuye.webapp.services;

import com.zhenyuye.webapp.dtos.bookDto.BookDTO;
import com.zhenyuye.webapp.dtos.bookDto.BookData;

import java.util.List;
import java.util.UUID;

public interface BookService {
    BookData createBook(BookDTO bookDTO, String auth);
    BookData getBook(UUID bookId);
    void removeBook(UUID bookId, String auth);
    List<BookData> getBooks();
}
