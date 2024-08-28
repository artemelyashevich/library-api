package com.elyashevich.library_service.service;

import com.elyashevich.library_service.api.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Interface for consuming messages from Kafka.
 */
public interface OrderConsumer {

    /**
     * Listen to a Kafka message.
     *
     * @param record the Kafka ConsumerRecord containing the key and value of the message
     */
    void listen(final ConsumerRecord<String, String> record) throws JsonProcessingException;
}