package com.todo.task.system.listener;


import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.todo.task.system.dto.TaskEvent;
import com.todo.task.system.service.TaskService;

@Component
public class TaskEventListener {

	Logger log = LoggerFactory.getLogger(TaskEventListener.class);
	
	@Autowired
	private TaskService todoService;


	@KafkaListener(topics = "task-event", groupId = "todo-group", containerFactory = "todoListener") 
	public void consumeTodoMessage(TaskEvent task, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
			@Header(KafkaHeaders.OFFSET) long offset) {
		try {
			log.info("KafkaConsumerService::consumeTaskMessage :: task {} , from {} , offset {}", task, topic,
					offset);
			 if(task.getEventType().equals("Create") && Objects.nonNull(task.getTask())) {
				 todoService.processCreateTask(task.getTask());
			 }else  if(task.getEventType().equals("Update") && Objects.nonNull(task.getTask())) {
				 todoService.processUpdateTask(task.getTask());				 
			 }
			
			log.info("KafkaConsumerService::consumeTaskMessage ::completed");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error("KafkaConsumerService::consumeTaskMessage:: Excception Occured {} " + e.getMessage());
		}
	}
}
