package com.elyashevich.book.service;

import com.elyashevich.book.api.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Interface for publishing orders using Kafka.
 */
public interface OrderPublisher {

    /**
     * Send a message for ordering a book.
     *
     * @param orderDto the data transfer object representing the order
     */
    void sendMessage(final OrderDto orderDto) throws JsonProcessingException;
}