package com.elyashevich.library_service.service.impl;

import com.elyashevich.library_service.api.dto.OrderDto;
import com.elyashevich.library_service.api.mapper.OrderMapper;
import com.elyashevich.library_service.service.OrderConsumer;
import com.elyashevich.library_service.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderConsumerImp implements OrderConsumer {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    @KafkaListener(
            topics = "order",
            groupId = "main_topic"
    )
    public void listen(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.debug("Try to get an order message: {}", record.value());
        var dto = deserializeFromJson(record.value());

        this.orderService.create(this.orderMapper.toEntity(dto));
        log.info("Order '{}' message has been successfully got.", dto);
    }

    private static OrderDto deserializeFromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(
                        new JavaTimeModule()
                                .addSerializer(
                                        LocalDateTime.class,
                                        new LocalDateTimeSerializer(DateTimeFormatter
                                                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                                        )
                                )
                );
        return objectMapper.readValue(json, OrderDto.class);
    }
}
