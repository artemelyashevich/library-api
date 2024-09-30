package com.elyashevich.book.service;

import com.elyashevich.book.entity.Book;
import com.elyashevich.book.exception.ResourceNotFoundException;
import com.elyashevich.book.repository.BookRepository;
import com.elyashevich.book.service.converter.BookConverter;
import com.elyashevich.book.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for testing the BookService.
 */
@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository repository;

    @Mock
    private BookConverter converter;

    /**
     * Test case for testing the getById method with an existing book.
     */
    @ParameterizedTest
    @MethodSource("provideBook")
    public void testGetById_ExistingBook(final Book book, final UUID id) {
        // Act
        when(this.repository.findById(id)).thenReturn(Optional.of(book));
        var foundBook = this.bookService.getById(id);

        // Assert
        assertEquals(book, foundBook);
        verify(this.repository, times(1)).findById(id);
    }

    /**
     * Test case for testing the getByIsbn method with an existing book.
     */
    @ParameterizedTest
    @MethodSource("provideBook")
    public void testGetByIsbn_ExistingBook(final Book book, UUID id) {
        // Act
        when(this.repository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));
        var foundBook = this.bookService.getByIsbn(book.getIsbn());

        // Assert
        assertEquals(foundBook.getId(), id);
        verify(this.repository, times(1)).findByIsbn(book.getIsbn());
    }

    /**
     * Test case for testing the getByIsbn method when it throws a ResourceNotFoundException.
     */
    @ParameterizedTest
    @EmptySource
    public void testGetByIsbn_throwsException(final String isbn) {
        assertThrows(ResourceNotFoundException.class, () -> this.bookService.getByIsbn(isbn));
    }

    /**
     * Test case for testing the getAll method.
     */
    @Test
    public void testGetAll() {
        // Act
        when(this.repository.findAll()).thenReturn(new ArrayList<Book>());
        var foundBooks = this.bookService.getAll();

        // Assert
        assertEquals(foundBooks.size(), 0);
        verify(this.repository, times(1)).findAll();
    }

    /**
     * Test case for testing the create method.
     */
    @ParameterizedTest
    @MethodSource("provideBook")
    public void testCreate(final Book book, final UUID bookId) {
        // Act
        when(this.repository.save(book)).thenReturn(book);
        var result = this.bookService.create(book);

        // Assert
        assertEquals(book, result);
        verify(this.repository, times(1)).save(book);
    }

    /**
     * Test case for testing the delete method.
     */
    @ParameterizedTest
    @MethodSource("provideBook")
    public void testDelete(final Book book, final UUID bookId) {

        // Act
        when(this.repository.findById(bookId)).thenReturn(Optional.of(book));
        this.bookService.delete(bookId);

        // Assert
        verify(this.repository, times(1)).delete(book);
    }

    private static Stream<Arguments> provideBook() {
        var bookId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        var book = new Book(
                bookId,
                "Title",
                "Description",
                "Genre",
                "Author",
                "123-1234567890",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return Stream.of(
                Arguments.of(book, bookId)
        );
    }
}
