package com.elyashevich.library_service.service;

import com.elyashevich.library_service.entity.OrderEntity;
import com.elyashevich.library_service.exception.ResourceNotFoundException;
import com.elyashevich.library_service.repository.OrderRepository;
import com.elyashevich.library_service.service.converter.OrderConverter;
import com.elyashevich.library_service.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LibraryServiceTests {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderConverter converter;

    @ParameterizedTest
    @MethodSource("provideOrder")
    public void testGetById_ExistingBook(final OrderEntity order, final UUID orderId) {
        // Act
        when(this.repository.findById(orderId)).thenReturn(Optional.of(order));
        var foundOrder = this.orderService.getById(orderId);

        // Assert
        assertEquals(order, foundOrder);
        verify(this.repository, times(1)).findById(orderId);
    }

    @Test
    public void testGetById_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> this.orderService.getById(UUID.randomUUID()));
    }

    @ParameterizedTest
    @MethodSource("provideOrder")
    public void testGetAll(final OrderEntity order, final UUID orderId) {
        // Act
        var dummyOrders = List.of(
                order
        );
        when(this.repository.findAll()).thenReturn(dummyOrders);
        var foundOrders = this.orderService.getAll();

        // Assert
        assertAll(
                () -> assertEquals(dummyOrders.size(), foundOrders.size()),
                () -> assertIterableEquals(dummyOrders, foundOrders)
        );
        verify(this.repository, times(1)).findAll();
    }

    @ParameterizedTest
    @MethodSource("provideOrder")
    public void testCreate(final OrderEntity order, final UUID orderId) {
        // Act
        when(this.repository.save(order)).thenReturn(order);
        var result = this.orderService.create(order);

        // Assert
        assertEquals(order, result);
        verify(this.repository, times(1)).save(order);
    }

    @ParameterizedTest
    @MethodSource("provideOrder")
    public void testDelete(final OrderEntity order, final UUID orderId) {
        // Act
        when(this.repository.findById(orderId)).thenReturn(Optional.of(order));
        this.orderService.delete(orderId);

        // Assert
        verify(this.repository, times(1)).delete(order);
    }

    private static Stream<Arguments> provideOrder() {
        var orderId = UUID.randomUUID();
        var order = new OrderEntity(
                orderId,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1L),
                null,
                null
        );
        return Stream.of(
                Arguments.of(order, orderId)
        );
    }
}
