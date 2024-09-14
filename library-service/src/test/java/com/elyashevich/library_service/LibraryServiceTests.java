package com.elyashevich.library_service;

import com.elyashevich.library_service.entity.OrderEntity;
import com.elyashevich.library_service.exception.ResourceNotFoundException;
import com.elyashevich.library_service.repository.OrderRepository;
import com.elyashevich.library_service.service.converter.OrderConverter;
import com.elyashevich.library_service.service.impl.OrderServiceImpl;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for testing the LibraryService.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class LibraryServiceTests {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderConverter converter;

    /**
     * Test case for testing the getById method with an existing order.
     */
    @Test
    public void testGetById_ExistingBook() {
        var orderId = UUID.randomUUID();
        var dummyOrder = getOrderExample(orderId);

        when(this.repository.findById(orderId)).thenReturn(Optional.of(dummyOrder));

        var foundOrder = this.orderService.getById(orderId);

        assertEquals(dummyOrder, foundOrder);

        verify(this.repository, times(1)).findById(orderId);
    }

    /**
     * Test case for testing the getById method when it throws a ResourceNotFoundException.
     */
    @Test
    public void testGetById_throwsException() {
        assertThrows(ResourceNotFoundException.class, () -> this.orderService.getById(UUID.randomUUID()));
    }

    /**
     * Test case for testing the getAll method.
     */
    @Test
    public void testGetAll() {
        var dummyOrders = List.of(
                getOrderExample(UUID.randomUUID())
        );

        when(this.repository.findAll()).thenReturn(dummyOrders);

        var foundOrders = this.orderService.getAll();

        assertEquals(dummyOrders.size(), foundOrders.size());
        assertIterableEquals(dummyOrders, foundOrders);

        verify(this.repository, times(1)).findAll();
    }

    /**
     * Test case for testing the create method.
     */
    @Test
    public void testCreate() {
        var orderId = UUID.randomUUID();
        var orderToCreate = getOrderExample(orderId);
        var createdOrder= getOrderExample(orderId);

        when(this.repository.save(orderToCreate)).thenReturn(createdOrder);

        var result = this.orderService.create(orderToCreate);

        assertEquals(createdOrder, result);

        verify(this.repository, times(1)).save(orderToCreate);
    }

    /**
     * Test case for testing the delete method.
     */
    @Test
    @Transactional
    public void testDelete() {
        var orderId = UUID.randomUUID();
        var orderToDelete = getOrderExample(orderId);

        when(this.repository.findById(orderId)).thenReturn(Optional.of(orderToDelete));

        this.orderService.delete(orderId);

        verify(this.repository, times(1)).delete(orderToDelete);
    }

    private OrderEntity getOrderExample(final UUID orderId) {
        return new OrderEntity(
                orderId,
                UUID.randomUUID(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1L),
                null,
                null
        );
    }
}
