package com.elyashevich.book.service.impl;

import com.elyashevich.book.entity.Book;
import com.elyashevich.book.exception.ResourceNotFoundException;
import com.elyashevich.book.repository.BookRepository;
import com.elyashevich.book.service.BookService;
import com.elyashevich.book.service.converter.BookConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookConverter converter;

    @Override
    public Book getById(final UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No such book with id = %s".formatted(id)));
    }

    @Override
    public List<Book> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Book create(final Book book) {
        log.debug("Attempting to create a new book: '{}'.", book);
        var newBook = this.repository.save(book);

        log.info("Book with ID '{}' has been created.", newBook.getId());
        return newBook;
    }

    @Transactional
    @Override
    public Book update(final UUID id, final Book newBook) {
        var oldBook = this.getById(id);
        log.debug("Attempting to update the old book with ID '{}' with the new book: '{}'.", id, newBook);

        var result = this.repository.save(this.converter.updateConverter(oldBook, newBook));
        log.info("Book with ID '{}' has been updated.", id);
        return result;
    }

    @Transactional
    @Override
    public void delete(final UUID id) {
        var book = this.getById(id);
        log.debug("Attempting to delete the book with ID '{}'.", id);

        this.repository.delete(book);
        log.info("Book with ID '{}' has been deleted.", id);
    }
}
