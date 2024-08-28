package com.elyashevich.book.api.controller;

import com.elyashevich.book.api.dto.BookDto;
import com.elyashevich.book.api.dto.OrderDto;
import com.elyashevich.book.api.mapper.BookMapper;
import com.elyashevich.book.api.validation.OnCreate;
import com.elyashevich.book.api.validation.OnUpdate;
import com.elyashevich.book.service.BookService;
import com.elyashevich.book.service.OrderPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final OrderPublisher orderPublisher;

    private final BookMapper mapper;

    @GetMapping
    public List<BookDto> getAll() {
        var books = this.bookService.getAll();
        return this.mapper.toDto(books);
    }

    @GetMapping("/{id}")
    public BookDto getById(@PathVariable("id") final UUID id) {
        var book = this.bookService.getById(id);
        return this.mapper.toDto(book);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@Validated(OnCreate.class) @RequestBody final BookDto dto) {
        var book = this.bookService.create(this.mapper.toEntity(dto));
        return this.mapper.toDto(book);
    }

    @PatchMapping("/{id}")
    public BookDto update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final BookDto dto
    ) {
        var book = this.bookService.update(id, this.mapper.toEntity(dto));
        return this.mapper.toDto(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final UUID id) {
        this.bookService.delete(id);
    }

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void order(@Validated(OnCreate.class) @RequestBody final OrderDto orderDto) throws JsonProcessingException {
        log.debug(orderDto.toString());
        this.orderPublisher.sendMessage(orderDto);
    }
}
