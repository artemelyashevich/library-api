package com.elyashevich.book.service.impl;

import com.elyashevich.book.api.dto.OrderDto;
import com.elyashevich.book.exception.ResourceNotFoundException;
import com.elyashevich.book.repository.BookRepository;
import com.elyashevich.book.service.OrderPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPublisherImpl implements OrderPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final BookRepository bookRepository;

    @Value("${application.kafka.topic:order}")
    private String topicName;

    @Override
    public void sendMessage(final OrderDto orderDto) throws JsonProcessingException {
        if (!this.bookRepository.existsById(orderDto.getBookId())) {
            throw new ResourceNotFoundException("No such book with id: %s.".formatted(orderDto.getBookId()));
        }
        log.debug("Try to send an order message: {}", orderDto);
        orderDto.setExpireIn(LocalDateTime.now().plusDays(5));
        var order = serializeToJson(orderDto);
        this.kafkaTemplate.send(topicName, order);
        log.info("Order '{}' message has been successfully sent.", orderDto);
    }

    private static String serializeToJson(OrderDto dto) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper
                .writer()
                .withDefaultPrettyPrinter()
                .writeValueAsString(dto);
    }
}
