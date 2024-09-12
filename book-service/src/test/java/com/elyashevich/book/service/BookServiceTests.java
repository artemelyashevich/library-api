package com.elyashevich.book.service;

import com.elyashevich.book.entity.Book;
import com.elyashevich.book.repository.BookRepository;
import com.elyashevich.book.service.converter.BookConverter;
import com.elyashevich.book.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BookServiceTests {

    @Autowired
    private BookServiceImpl bookService;

    @MockBean
    private BookRepository repository;

    @Mock
    private BookConverter converter;

    @Test
    public void testGetById() {
        var bookId = UUID.randomUUID();
        var dummyBook = getBookExample(bookId);

        when(repository.findById(bookId)).thenReturn(Optional.of(dummyBook));

        var foundBook = bookService.getById(bookId);
        assertEquals(dummyBook, foundBook);
    }

    @Test
    public void testGetByIsbn_ExistingBook() {
        var bookId = UUID.randomUUID();
        Book dummyBook = getBookExample(bookId);

        when(repository.findByIsbn(dummyBook.getIsbn())).thenReturn(Optional.of(dummyBook));

        Book foundBook = bookService.getByIsbn(dummyBook.getIsbn());
        assertEquals(dummyBook, foundBook);
    }

    @Test
    public void testGetAll() {
        var dummyBooks = List.of(
                getBookExample(UUID.randomUUID())
        );
        when(repository.findAll()).thenReturn(dummyBooks);
        var foundBooks = bookService.getAll();
        assertEquals(dummyBooks.size(), foundBooks.size());
        assertIterableEquals(dummyBooks, foundBooks);
    }

    @Test
    public void testCreate() {
        var bookId = UUID.randomUUID();
        var bookToCreate = getBookExample(bookId);
        var createdBook = getBookExample(bookId);

        when(repository.save(bookToCreate)).thenReturn(createdBook);

        var result = bookService.create(bookToCreate);

        verify(repository, times(1)).save(bookToCreate);
        assertEquals(createdBook, result);
    }

    @Test
    @Transactional
    public void testUpdate() {
        var bookId = UUID.randomUUID();
        Book oldBook = getBookExample(bookId);
        Book newBook = new Book(
                bookId,
                "Updated Book",
                "New Description",
                "New Genre",
                "New Author",
                "978-3-16-148410-1",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(bookService.getById(bookId)).thenReturn(oldBook);
        when(converter.updateConverter(oldBook, newBook)).thenReturn(newBook);
        when(repository.save(newBook)).thenReturn(newBook);

        Book result = bookService.update(bookId, newBook);

        verify(bookService, times(1)).getById(bookId);
        verify(converter, times(1)).updateConverter(oldBook, newBook);
        verify(repository, times(1)).save(newBook);

        assertEquals(newBook.getTitle(), result.getTitle());
        assertEquals(newBook.getDescription(), result.getDescription());
        assertEquals(newBook.getGenre(), result.getGenre());
        assertEquals(newBook.getAuthor(), result.getAuthor());
        assertEquals(newBook.getIsbn(), result.getIsbn());
    }

    @Test
    @Transactional
    public void testDelete() {
        var bookId = UUID.randomUUID();
        var bookToDelete = getBookExample(bookId);

        when(bookService.getById(bookId)).thenReturn(bookToDelete);

        bookService.delete(bookId);

        verify(bookService, times(1)).getById(bookId);
        verify(repository, times(1)).delete(bookToDelete);
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
}
