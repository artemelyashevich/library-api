package com.elyashevich.book_service.repository;

import com.elyashevich.book_service.domain.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<BookEntity, UUID> {
}
