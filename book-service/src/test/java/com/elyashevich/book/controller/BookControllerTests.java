package com.elyashevich.book.controller;

import com.elyashevich.book.api.controller.impl.BookControllerImpl;
import com.elyashevich.book.api.dto.BookDto;
import com.elyashevich.book.api.mapper.BookMapper;
import com.elyashevich.book.entity.Book;
import com.elyashevich.book.service.BookService;
import com.elyashevich.book.service.impl.OrderPublisherImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for testing the BookController.
 */
@WebMvcTest(BookControllerImpl.class)
class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private OrderPublisherImpl publisher;

    @MockBean
    private BookMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("provideBook")
    void testGetAll(final Book book, final UUID bookId) throws Exception {
        var mockBookDtos = List.of(getBookDtoExample(bookId));
        when(this.bookService.getAll()).thenReturn(List.of(book));
        when(this.mapper.toDto(List.of(book))).thenReturn(mockBookDtos);
        this.mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk());
        verify(this.bookService, times(1)).getAll();
        verify(this.mapper, times(1)).toDto(List.of(book));
    }

    @ParameterizedTest
    @MethodSource("provideBook")
    void testGetById(final Book book, final UUID bookId) throws Exception {
        when(this.bookService.getById(bookId)).thenReturn(book);
        when(this.mapper.toDto(book)).thenReturn(getBookDtoExample(bookId));

        this.mockMvc.perform(get("/api/v1/books/{id}", bookId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(book.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(book.getGenre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()));
        verify(this.bookService, times(1)).getById(bookId);
        verify(this.mapper, times(1)).toDto(book);
    }

    @ParameterizedTest
    @MethodSource("provideBook")
    void testGetByIsbn(final Book book, final UUID bookId) throws Exception {
        var isbn = "123-1234567890";
        when(this.bookService.getByIsbn(isbn)).thenReturn(book);
        when(this.mapper.toDto(book)).thenReturn(getBookDtoExample(bookId));
        this.mockMvc.perform(get("/api/v1/books/isbn/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(book.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(book.getGenre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn));
        verify(this.bookService, times(1)).getByIsbn(isbn);
        verify(this.mapper, times(1)).toDto(book);
    }

    @ParameterizedTest
    @MethodSource("provideBook")
    void testCreate(final Book book, final UUID bookId) throws Exception {
        var bookDto = getBookDtoExample(bookId);

        when(this.mapper.toEntity(bookDto)).thenReturn(book);
        when(this.bookService.create(any(Book.class))).thenReturn(book);
        when(this.mapper.toDto(book)).thenReturn(bookDto);
        this.mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(book.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(book.getGenre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(book.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(book.getIsbn()));
        verify(this.bookService, times(1)).create(any(Book.class));
        verify(this.mapper, times(1)).toDto(book);
        verify(this.mapper, times(1)).toEntity(bookDto);
    }

    @Test
    void testUpdate() throws Exception {
        var bookId = UUID.randomUUID();
        var bookDto = getUpdatedBookDto();
        var mockBook = getUpdatedBook(bookId);

        when(this.mapper.toEntity(bookDto)).thenReturn(mockBook);
        when(this.bookService.update(bookId, mockBook)).thenReturn(mockBook);
        when(this.mapper.toDto(mockBook)).thenReturn(bookDto);

        this.mockMvc.perform(patch("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Updated Genre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Updated Author"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("123-1234567890"));
        verify(this.mapper, times(1)).toEntity(bookDto);
        verify(this.bookService, times(1)).update(bookId, mockBook);
        verify(this.mapper, times(1)).toDto(mockBook);
    }

    @Test
    void testDelete() throws Exception {
        var bookId = UUID.randomUUID();

        doNothing().when(this.bookService).delete(any(UUID.class));

        this.mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                .andExpect(status().isNoContent());
        verify(this.bookService, times(1)).delete(any(UUID.class));
    }

    private static Stream<Arguments> provideBook() {
        var bookId = UUID.randomUUID();
        var book = new Book(
                bookId,
                "Title",
                "Description",
                "Genre",
                "Author",
                "123-1234567890",
                null,
                null
        );
        return Stream.of(
                Arguments.of(book, bookId)
        );
    }

    private static BookDto getBookDtoExample(final UUID id) {
        return new BookDto(
                id,
                "Title",
                "Description",
                "Genre",
                "Author",
                "123-1234567890",
                null,
                null
        );
    }

    private static Book getUpdatedBook(final UUID bookId) {
        return new Book(
                bookId,
                "Updated Title",
                "Updated Description",
                "Updated Genre",
                "Updated Author",
                "123-1234567890",
                null,
                null
        );
    }

    private static BookDto getUpdatedBookDto() {
        return new BookDto(
                null,
                "Updated Title",
                "Updated Description",
                "Updated Genre",
                "Updated Author",
                "123-1234567890",
                null,
                null
        );
    }
}
