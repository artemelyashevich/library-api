package com.elyashevich.book_service.service.impl;

import com.elyashevich.book_service.converter.BookConverter;
import com.elyashevich.book_service.domain.entity.BookEntity;
import com.elyashevich.book_service.domain.exception.NotFoundException;
import com.elyashevich.book_service.repository.BookRepository;
import com.elyashevich.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookConverter converter;

    @Override
    public BookEntity getById(final UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such book with id = %s".formatted(id)));
    }

    @Override
    public List<BookEntity> getAll() {
        return this.repository.findAll();
    }

    @Override
    public BookEntity create(final BookEntity book) {
        return this.repository.save(book);
    }

    @Override
    public BookEntity update(final UUID id, final BookEntity book) {
        var bookEntity = this.getById(id);
        return this.repository.save(
                this.converter.updateConverter(bookEntity, book)
        );
    }

    @Override
    public void delete(final UUID id) {
        this.repository.delete(
                this.getById(id)
        );
    }
}
