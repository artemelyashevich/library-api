package com.elyashevich.book.repository;

import com.elyashevich.book.entity.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;

    @ParameterizedTest
    @MethodSource("provideBook")
    void bookRepository_save(final Book book, final String isbn) {
        // Act
        var savedBook = this.bookRepository.save(book);

        // Assert
        assertAll(
                () -> assertThat(savedBook).isNotNull(),
                () -> assertThat(savedBook.getId()).isNotNull()
        );
    }

    @Test
    void bookRepository_findAll() {
        // Act
        var books = this.bookRepository.findAll();

        // Assert
        assertAll(
                () -> assertThat(books).isNotNull(),
                () -> assertThat(books.size()).isEqualTo(5)
        );
    }

    @Test
    void bookRepository_findById() {
        // Act 
        var bookId = "f47ac10b-58cc-4372-a567-0e02b2c3d475";
        var book = this.bookRepository.findById(UUID.fromString(bookId)).get();

        // Asserts
        assertAll(
                () -> assertThat(book.getId()).isNotNull(),
                () -> assertThat(book.getTitle()).isEqualTo("The Catcher in the Rye")
        );
    }

    @ParameterizedTest
    @MethodSource("provideBook")
    void bookRepository_findByIsbn(final Book book, final String isbn) {
        // Act
        this.bookRepository.save(book);
        var actualBook = this.bookRepository.findByIsbn(isbn).get();

        // Assert
        assertAll(
                () -> assertThat(actualBook).isNotNull(),
                () -> assertThat(actualBook.getIsbn()).isEqualTo(isbn)
        );
    }

    private static Stream<Arguments> provideBook() {
        var book = new Book();
        var isbn = "111-1111111111";
        book.setIsbn(isbn);

        return Stream.of(
                Arguments.of(book, isbn)
        );
    }
}
