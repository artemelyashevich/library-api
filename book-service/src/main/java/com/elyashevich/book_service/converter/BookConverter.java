package com.elyashevich.book_service.converter;

import com.elyashevich.book_service.domain.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    public BookEntity updateConverter(BookEntity oldBook, BookEntity newBook) {
        oldBook.setTitle(newBook.getTitle());
        oldBook.setDescription(newBook.getDescription());
        oldBook.setAuthor(newBook.getAuthor());
        oldBook.setGenre(newBook.getGenre());
        return oldBook;
    }
}
