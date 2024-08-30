package com.elyashevich.book.api.controller;

import com.elyashevich.book.api.dto.BookDto;
import com.elyashevich.book.api.dto.ExceptionBody;
import com.elyashevich.book.api.dto.OrderDto;
import com.elyashevich.book.api.mapper.BookMapper;
import com.elyashevich.book.api.validation.OnCreate;
import com.elyashevich.book.api.validation.OnUpdate;
import com.elyashevich.book.service.BookService;
import com.elyashevich.book.service.OrderPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book Controller", description = "APIs for book service")
public class BookController {

    private final BookService bookService;
    private final OrderPublisher orderPublisher;

    private final BookMapper mapper;

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get all books",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )
            }
    )
    @GetMapping
    public List<BookDto> getAll() {
        var books = this.bookService.getAll();
        return this.mapper.toDto(books);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get book by id",
                            content = @Content(schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book was not found",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public BookDto getById(@PathVariable("id") final UUID id) {
        var book = this.bookService.getById(id);
        return this.mapper.toDto(book);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get book by id",
                            content = @Content(schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book was not found",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@Validated(OnCreate.class) @RequestBody final BookDto dto) {
        var book = this.bookService.create(this.mapper.toEntity(dto));
        return this.mapper.toDto(book);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update book by id",
                            content = @Content(schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book was not found",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @PatchMapping("/{id}")
    public BookDto update(
            @PathVariable("id") final UUID id,
            @Validated(OnUpdate.class) @RequestBody final BookDto dto
    ) {
        var book = this.bookService.update(id, this.mapper.toEntity(dto));
        return this.mapper.toDto(book);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Delete book by id",
                            content = @Content(schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book was not found",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") final UUID id) {
        this.bookService.delete(id);
    }

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Make order"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book was not found",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid date format",
                            content = @Content(schema = @Schema(implementation = ExceptionBody.class))
                    )
            }
    )
    @PostMapping("/order")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void order(@Validated(OnCreate.class) @RequestBody final OrderDto orderDto) throws JsonProcessingException {
        this.orderPublisher.sendMessage(orderDto);
    }
}
