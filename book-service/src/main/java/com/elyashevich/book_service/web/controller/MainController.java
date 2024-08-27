package com.elyashevich.book_service.web.controller;

import com.elyashevich.book_service.domain.entity.BookEntity;
import com.elyashevich.book_service.service.BookService;
import com.elyashevich.book_service.web.dto.BookDto;
import com.elyashevich.book_service.web.dto.OrderDto;
import com.elyashevich.book_service.web.dto.mapper.BookMapper;
import com.elyashevich.book_service.web.validation.OnCreate;
import com.elyashevich.book_service.web.validation.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class MainController {

    private final BookService bookService;
    private final BookMapper mapper;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAll() {
        return ResponseEntity
                .ok()
                .body(
                        this.mapper.toDto(
                                this.bookService.getAll()
                        )
                );
    }

    @PostMapping
    public ResponseEntity<BookDto> create(
            @Validated(OnCreate.class) @RequestBody final BookDto dto,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        var book = this.bookService.create(
                this.mapper.toEntity(dto)
        );
        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .replacePath("/api/v1/books/{id}")
                                .build(Map.of("id", book.getId()))
                )
                .body(
                        this.mapper.toDto(book)
                );
    }

    @PatchMapping("{id}")
    public ResponseEntity<BookDto> update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final BookDto dto,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        var book = this.bookService.update(
                id,
                this.mapper.toEntity(dto)
        );
        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .replacePath("/api/v1/books/{id}")
                                .build(Map.of("id", book.getId()))
                )
                .body(
                        this.mapper.toDto(book)
                );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final UUID id) {
        this.bookService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/order")
    public ResponseEntity<Void> order(@Validated(OnCreate.class) final OrderDto orderDto) {
        return ResponseEntity
                .accepted()
                .build();
    }
}
