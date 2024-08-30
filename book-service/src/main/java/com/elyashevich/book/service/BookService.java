package com.elyashevich.book.service;

import com.elyashevich.book.entity.Book;

import java.util.List;
import java.util.UUID;

/**
 * Interface for managing Book entities.
 */
public interface BookService {

    /**
     * Retrieve a book by its unique identifier.
     *
     * @param id the unique identifier of the book
     * @return the book with the specified ID
     */
    Book getById(final UUID id);

    /**
     * Retrieve a book by its unique isbn.
     *
     * @param isbn the unique identifier of the book
     * @return the book with the specified ID
     */
    Book getByIsbn(final String isbn);

    /**
     * Retrieve all books available.
     *
     * @return a list of all books
     */
    List<Book> getAll();

    /**
     * Create a new book.
     *
     * @param book the book to be created
     * @return the created book
     */
    Book create(final Book book);

    /**
     * Update an existing book with the specified ID.
     *
     * @param id   the unique identifier of the book to be updated
     * @param book the updated book information
     * @return the updated book
     */
    Book update(final UUID id, final Book book);

    /**
     * Delete a book by its unique identifier.
     *
     * @param id the unique identifier of the book to be deleted
     */
    void delete(final UUID id);
}