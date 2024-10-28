package com.todo.task.system.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.todo.task.system.dto.TaskEvent;

@Configuration
public class KafkaConfig {
	public static final String TODO_TOPIC = "todo-topic";
//
//	@Bean
//	public ProducerFactory<String, TaskEvent> producerFactory() {
//	
//		Map<String, Object> config = new HashMap<>();
//
//		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
//
//		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//		return new DefaultKafkaProducerFactory<>(config);
//	}
//
//	@Bean
//	public KafkaTemplate<String, TaskEvent> kafkaTemplate() {
//		return new KafkaTemplate<>(producerFactory());
//	}
//
//	@Bean
//	public NewTopic OrderTopic() {
//		return new NewTopic(TODO_TOPIC, 2, (short) 1);
//	}
}