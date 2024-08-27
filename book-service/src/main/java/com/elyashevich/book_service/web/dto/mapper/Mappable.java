package com.elyashevich.book_service.web.dto.mapper;

import java.util.List;

public interface Mappable<T, D>{
    D toDto(T entity);

    List<D> toDto(List<T> entities);

    T toEntity(D dto);
}
