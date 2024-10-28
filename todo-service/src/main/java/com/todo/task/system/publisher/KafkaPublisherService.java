package com.todo.task.system.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.todo.task.system.dto.TaskEvent;


@Component
public class KafkaPublisherService {

	Logger log = LoggerFactory.getLogger(KafkaPublisherService.class);

	private final KafkaTemplate<String, TaskEvent> kafkaTemplate;

	@Autowired
	public KafkaPublisherService(KafkaTemplate<String, TaskEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Retryable(maxAttempts = 3)
	public void publishTaskDetails(TaskEvent event) {
		Message<TaskEvent> message = MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, "task-event")
				.build();
		
		kafkaTemplate.send(message);
		log.info("Kafka published message::", message);
	}
}
