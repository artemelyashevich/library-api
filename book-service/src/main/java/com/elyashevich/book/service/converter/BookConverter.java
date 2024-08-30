package com.elyashevich.book.service.converter;

import com.elyashevich.book.entity.Book;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class BookConverter {

    @NonNull
    public Book updateConverter(final Book oldBook, final Book newBook) {
        oldBook.setTitle(newBook.getTitle());
        oldBook.setDescription(newBook.getDescription());
        oldBook.setAuthor(newBook.getAuthor());
        oldBook.setGenre(newBook.getGenre());
        oldBook.setIsbn(newBook.getIsbn());
        return oldBook;
    }
}
