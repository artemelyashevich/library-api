package com.elyashevich.book_service.web.dto.mapper;

import com.elyashevich.book_service.domain.entity.BookEntity;
import com.elyashevich.book_service.web.dto.BookDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper extends Mappable<BookEntity, BookDto> {
}
