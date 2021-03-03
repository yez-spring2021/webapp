package com.zhenyuye.webapp.repositories;

import com.zhenyuye.webapp.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findFilesByBookId(String bookId);
    boolean existsByFileName(String filename);
}
