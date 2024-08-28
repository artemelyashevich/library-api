package com.elyashevich.book.api.mapper;

import com.elyashevich.book.api.dto.BookDto;
import com.elyashevich.book.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper extends Mappable<Book, BookDto> {
}
