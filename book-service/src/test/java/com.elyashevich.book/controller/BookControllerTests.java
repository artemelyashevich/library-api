package com.elyashevich.book.controller;

import com.elyashevich.book.api.dto.BookDto;
import com.elyashevich.book.api.mapper.BookMapper;
import com.elyashevich.book.entity.Book;
import com.elyashevich.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAll() throws Exception {
        var id = UUID.randomUUID();
        var books = List.of(getBookExample(id));
        var mockBookDtos = List.of(getBookDtoExample(id));
        when(bookService.getAll()).thenReturn(books);
        when(mapper.toDto(books)).thenReturn(mockBookDtos);
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetById() throws Exception {
        var bookId = UUID.randomUUID();
        var mockBook = getBookExample(bookId);

        when(bookService.getById(bookId)).thenReturn(mockBook);
        when(mapper.toDto(mockBook)).thenReturn(getBookDtoExample(bookId));

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(mockBook.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(mockBook.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(mockBook.getGenre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(mockBook.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(mockBook.getIsbn()));
    }

    @Test
    public void testGetByIsbn() throws Exception {
        var isbn = "123-1234567890";
        var bookId = UUID.randomUUID();
        var mockBook = getBookExample(bookId);

        when(bookService.getByIsbn(isbn)).thenReturn(mockBook);
        when(mapper.toDto(mockBook)).thenReturn(getBookDtoExample(bookId));
        mockMvc.perform(get("/api/books/isbn/{isbn}", isbn))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(mockBook.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(mockBook.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(mockBook.getGenre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(mockBook.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(isbn));
    }

    @Test
    public void testCreate() throws Exception {
        var bookId = UUID.randomUUID();
        var mockBook = getBookExample(bookId);
        var bookDto = getBookDtoExample(bookId);

        when(bookService.create(any(Book.class))).thenReturn(mockBook);
        when(mapper.toEntity(bookDto)).thenReturn(mockBook);
        when(mapper.toDto(mockBook)).thenReturn(bookDto);
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(mockBook.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(mockBook.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(mockBook.getGenre()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(mockBook.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value(mockBook.getIsbn()));
    }

    @Test
    public void testUpdate() throws Exception {
        var bookId = UUID.randomUUID();
        var bookDto = new BookDto(
                bookId,
                "Updated Title",
                "Updated Description",
                "Updated Genre",
                "Updated Author",
                "123-1234567890",
                null,
                null
        );
        var mockBook = new Book(
                bookId,
                "Title",
                "Description",
                "Genre",
                "Author",
                "123-1234567890",
                null,
                null
        );

        when(bookService.update(bookId, any(Book.class))).thenReturn(mockBook);
        when(mapper.toEntity(bookDto)).thenReturn(new Book(
                        bookId,
                        "Updated Title",
                        "Updated Description",
                        "Updated Genre",
                        "Updated Author",
                        "123-1234567890",
                        null,
                        null
                )
        );
        when(mapper.toDto(mockBook)).thenReturn(new BookDto(
                        bookId,
                        "Updated Title",
                        "Updated Description",
                        "Updated Genre",
                        "Updated Author",
                        "123-1234567890",
                        null,
                        null
                )
        );

        mockMvc.perform(patch("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Updated Genre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Updated Author"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("123-1234567890"));
    }

    @Test
    public void testDelete() throws Exception {
        UUID bookId = UUID.randomUUID();

        doNothing().when(bookService).delete(any(UUID.class));

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }

    private static Book getBookExample(final UUID id) {
        return new Book(
                id,
                "Title",
                "Description",
                "Genre",
                "Author",
                "123-1234567890",
                LocalDateTime.now(),
                LocalDateTime.now()
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
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
