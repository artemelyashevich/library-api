package com.elyashevich.library_service;

import com.elyashevich.library_service.api.dto.OrderDto;
import com.elyashevich.library_service.api.mapper.OrderMapper;
import com.elyashevich.library_service.entity.OrderEntity;
import com.elyashevich.library_service.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for testing the LibraryController.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for testing the getAll method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetAll() throws Exception {
        var id = UUID.randomUUID();
        var orders = List.of(getOrderExample(id));
        var mockOrderDtos = List.of(getOrderDtoExample(id));
        when(this.orderService.getAll()).thenReturn(orders);
        when(this.mapper.toDto(orders)).thenReturn(mockOrderDtos);
        this.mockMvc.perform(get("/api/v1/library"))
                .andExpect(status().isOk());
        verify(this.orderService, times(1)).getAll();
        verify(this.mapper, times(1)).toDto(orders);
    }

    /**
     * Test case for testing the getById method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetById() throws Exception {
        var orderId = UUID.randomUUID();
        var mockOrder = getOrderExample(orderId);

        when(this.orderService.getById(orderId)).thenReturn(mockOrder);
        when(this.mapper.toDto(mockOrder)).thenReturn(getOrderDtoExample(orderId));

        this.mockMvc.perform(get("/api/v1/library/{id}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(orderId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(mockOrder.getBookId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expireIn").value(mockOrder.getExpireIn()));
        verify(this.orderService, times(1)).getById(orderId);
    }

    /**
     * Test case for testing the create method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreate() throws Exception {
        var orderId = UUID.randomUUID();
        var bookId = UUID.randomUUID();
        var orderIn = LocalDateTime.now();
        var mockOrder = new OrderEntity(
                orderId,
                bookId,
                orderIn.plusDays(1),
                orderIn,
                null,
                null
        );
        var orderDto = new OrderDto(
                orderId,
                bookId,
                orderIn.plusDays(1),
                orderIn,
                null,
                null
        );

        when(this.mapper.toEntity(orderDto)).thenReturn(mockOrder);
        when(this.orderService.create(any(OrderEntity.class))).thenReturn(mockOrder);
        when(this.mapper.toDto(mockOrder)).thenReturn(orderDto);
        this.mockMvc.perform(post("/api/v1/library")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(orderId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(orderDto.bookId().toString()));
        verify(this.orderService, times(1)).create(any(OrderEntity.class));
        verify(this.mapper, times(1)).toDto(mockOrder);
        verify(this.mapper, times(1)).toEntity(orderDto);
    }

    /**
     * Test case for testing the update method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testUpdate() throws Exception {
        var orderId = UUID.randomUUID();
        var bookId = UUID.randomUUID();
        var orderIn = LocalDateTime.now();
        var mockOrder = new OrderEntity(
                orderId,
                bookId,
                orderIn.plusDays(1),
                orderIn,
                null,
                null
        );
        var orderDto = new OrderDto(
                orderId,
                bookId,
                orderIn.plusDays(1),
                orderIn,
                null,
                null
        );

        when(this.mapper.toEntity(orderDto)).thenReturn(mockOrder);
        when(this.orderService.update(bookId, mockOrder)).thenReturn(mockOrder);
        when(this.mapper.toDto(mockOrder)).thenReturn(orderDto);

        this.mockMvc.perform(patch("/api/v1/library/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(orderId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(orderDto.bookId().toString()));
        verify(this.mapper, times(1)).toEntity(orderDto);
        verify(this.orderService, times(1)).update(bookId, mockOrder);
    }

    /**
     * Test case for testing the delete method.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void testDelete() throws Exception {
        var orderId = UUID.randomUUID();

        doNothing().when(this.orderService).delete(any(UUID.class));

        this.mockMvc.perform(delete("/api/v1/library/{id}", orderId))
                .andExpect(status().isNoContent());
        verify(this.orderService, times(1)).delete(any(UUID.class));
    }

    private static OrderEntity getOrderExample(final UUID orderId) {
        return new OrderEntity(
                orderId,
                null,
                null,
                null,
                null,
                null
        );
    }

    private static OrderDto getOrderDtoExample(final UUID orderId) {
        return new OrderDto(
                orderId,
                null,
                null,
                null,
                null,
                null
        );
    }
}
