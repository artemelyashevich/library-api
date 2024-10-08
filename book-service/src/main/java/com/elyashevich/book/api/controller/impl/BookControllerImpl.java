package com.elyashevich.book.api.controller.impl;

import com.elyashevich.book.api.controller.BookController;
import com.elyashevich.book.api.dto.BookDto;
import com.elyashevich.book.api.dto.OrderDto;
import com.elyashevich.book.api.mapper.BookMapper;
import com.elyashevich.book.api.validation.OnCreate;
import com.elyashevich.book.api.validation.OnUpdate;
import com.elyashevich.book.service.BookService;
import com.elyashevich.book.service.OrderPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookControllerImpl implements BookController {

    private final BookService bookService;
    private final OrderPublisher orderPublisher;

    private final BookMapper mapper;

    @Override
    public List<BookDto> getAll() {
        var books = this.bookService.getAll();
        return this.mapper.toDto(books);
    }

    @Override
    public BookDto getById(@PathVariable("id") final UUID id) {
        var book = this.bookService.getById(id);
        return this.mapper.toDto(book);
    }

    @Override
    public BookDto getByIsbn(@PathVariable("isbn") final String isbn) {
        var book = this.bookService.getByIsbn(isbn);
        return this.mapper.toDto(book);
    }

    @Override
    public BookDto create(@Validated(OnCreate.class) @RequestBody final BookDto dto) {
        var book = this.bookService.create(this.mapper.toEntity(dto));
        return this.mapper.toDto(book);
    }

    @Override
    public BookDto update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final BookDto dto
    ) {
        var book = this.bookService.update(id, this.mapper.toEntity(dto));
        return this.mapper.toDto(book);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final UUID id) {
        this.bookService.delete(id);
    }

    @Override
    public void order(@Validated(OnCreate.class) @RequestBody final OrderDto orderDto) throws JsonProcessingException {
        this.orderPublisher.sendMessage(orderDto);
    }
}
