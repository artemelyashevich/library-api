package com.elyashevich.book_service.service;

import com.elyashevich.book_service.domain.entity.BookEntity;

import java.util.List;
import java.util.UUID;

public interface BookService {

    BookEntity getById(UUID id);

    List<BookEntity> getAll();

    BookEntity create(BookEntity book);

    BookEntity update(UUID id, BookEntity book);

    void delete(UUID id);
}
