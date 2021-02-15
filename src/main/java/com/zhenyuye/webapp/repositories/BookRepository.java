package com.zhenyuye.webapp.repositories;

import com.zhenyuye.webapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
