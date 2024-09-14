package com.elyashevich.book;

import com.elyashevich.book.entity.Book;
import com.elyashevich.book.exception.ResourceNotFoundException;
import com.elyashevich.book.repository.BookRepository;
import com.elyashevich.book.service.converter.BookConverter;
import com.elyashevich.book.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for testing the BookService.
 */
@SpringBootTest
@AutoConfigureMockMvc
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
    @Test
    public void testGetById_ExistingBook() {
        var bookId = UUID.randomUUID();
        var dummyBook = getBookExample(bookId);

        when(this.repository.findById(bookId)).thenReturn(Optional.of(dummyBook));

        var foundBook = this.bookService.getById(bookId);

        assertEquals(dummyBook, foundBook);

        verify(this.repository, times(1)).findById(bookId);
    }

    /**
     * Test case for testing the getById method when it throws a ResourceNotFoundException.
     */
    @Test
    public void testGetById_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> this.bookService.getById(UUID.randomUUID()));
    }

    /**
     * Test case for testing the getByIsbn method with an existing book.
     */
    @Test
    public void testGetByIsbn_ExistingBook() {
        var bookId = UUID.randomUUID();
        var dummyBook = getBookExample(bookId);

        when(this.repository.findByIsbn(dummyBook.getIsbn())).thenReturn(Optional.of(dummyBook));

        var foundBook = this.bookService.getByIsbn(dummyBook.getIsbn());
        assertEquals(dummyBook, foundBook);

        verify(this.repository, times(1)).findByIsbn(dummyBook.getIsbn());
    }

    /**
     * Test case for testing the getByIsbn method when it throws a ResourceNotFoundException.
     */
    @Test
    public void testGetByIsbn_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> this.bookService.getByIsbn("111-111111111"));
    }

    /**
     * Test case for testing the getAll method.
     */
    @Test
    public void testGetAll() {
        var dummyBooks = List.of(
                getBookExample(UUID.randomUUID())
        );

        when(this.repository.findAll()).thenReturn(dummyBooks);

        var foundBooks = this.bookService.getAll();

        assertEquals(dummyBooks.size(), foundBooks.size());
        assertIterableEquals(dummyBooks, foundBooks);

        verify(this.repository, times(1)).findAll();
    }

    /**
     * Test case for testing the create method.
     */
    @Test
    public void testCreate() {
        var bookId = UUID.randomUUID();
        var bookToCreate = getBookExample(bookId);
        var createdBook = getBookExample(bookId);

        when(this.repository.save(bookToCreate)).thenReturn(createdBook);

        var result = this.bookService.create(bookToCreate);

        assertEquals(createdBook, result);

        verify(this.repository, times(1)).save(bookToCreate);
    }

    /**
     * Test case for testing the delete method.
     */
    @Test
    @Transactional
    public void testDelete() {
        var bookId = UUID.randomUUID();
        var bookToDelete = getBookExample(bookId);

        when(this.repository.findById(bookId)).thenReturn(Optional.of(bookToDelete));

        this.bookService.delete(bookId);

        verify(this.repository, times(1)).delete(bookToDelete);
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
