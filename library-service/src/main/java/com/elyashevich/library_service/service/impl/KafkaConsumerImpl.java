package com.elyashevich.library_service.service.impl;

import com.elyashevich.library_service.api.dto.OrderDto;
import com.elyashevich.library_service.service.KafkaConsumer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerImpl implements KafkaConsumer {

    private static final Gson gson = new GsonBuilder().create();

    @Override
    @KafkaListener(
            topics="order",
            groupId = "main_topic"
    )
    public void listen(ConsumerRecord<String, String> record) {
        //final var data = gson.fromJson(record.value(), OrderDto.class);
        System.out.println("DATA FROM ORDER SERVICE --->" + record.value());
    }
}
