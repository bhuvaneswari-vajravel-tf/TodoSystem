package com.todo.task.system.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todo.task.system.constants.TodoSystemConstants;
import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskEvent;
import com.todo.task.system.entity.TaskEntity;
import com.todo.task.system.exception.BadRequestException;
import com.todo.task.system.exception.DuplicateTaskNameException;
import com.todo.task.system.exception.TaskNotFoundException;
import com.todo.task.system.publisher.KafkaPublisherService;
import com.todo.task.system.repository.TaskRepository;
import com.todo.task.system.service.TaskService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

	@Autowired
	TaskRepository taskRepo;
	
	@Autowired
	KafkaPublisherService kafkaPublisher;


	private static final List<String> status = Stream
			.of(TodoSystemConstants.CREATED, TodoSystemConstants.PENDING, TodoSystemConstants.COMPLETED)
			.collect(Collectors.toList());

	@Override
	public TaskDetailsDTO createTask(TaskDetailsDTO taskDetailsDto) {
		TaskDetailsDTO taskDetail = new TaskDetailsDTO();
		log.info("TaskServiceImpl:: createTask:: entering create task method ");
		validateTask(taskDetailsDto);

		Optional<TaskEntity> taskEntity = taskRepo.findByTaskNameIgnoreCase(taskDetailsDto.getTaskName().trim());

		taskEntity.ifPresent(entity -> {
			throw new DuplicateTaskNameException("Task name " + taskDetailsDto.getTaskName().trim() + " already exist");
		});

		taskDetailsDto.setTaskStatus(TodoSystemConstants.CREATED);
		taskDetailsDto.setActive(true);
		TaskEntity entity = taskRepo.save(taskDtoToEntityConverter(taskDetailsDto));
		taskDetail = entityToDtoConvertor(entity);
		
		TaskEvent taskEvent = TaskEvent.builder()
				.eventType("Create")
				.task(taskDetail)
				.build();
		kafkaPublisher.publishTaskDetails(taskEvent);
		
		return taskDetail;
	}

	private void validateTask(TaskDetailsDTO taskDetailsDto) {

		LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
   

		if (taskDetailsDto != null) {
			

		    System.out.println("Start date: " + taskDetailsDto.getStartDate());
		    
		    System.out.println("now date: " + now);
		    
			if (taskDetailsDto.getTaskName() == null || taskDetailsDto.getTaskName().isBlank())
				throw new BadRequestException("task name is mandatory");
			if (taskDetailsDto.getTaskDescription() == null || taskDetailsDto.getTaskDescription().isBlank())
				throw new BadRequestException("Task description is mandatory");
			if (taskDetailsDto.getEffort() <= 0)
				throw new BadRequestException("Task effort is invalid");
			if (taskDetailsDto.getStartDate() == null || (taskDetailsDto.getStartDate().isBefore(now)))
				throw new BadRequestException("Start date is mandatory");
			if (taskDetailsDto.getEndDate() == null || (taskDetailsDto.getEndDate().isBefore(now)))
				throw new BadRequestException("End date is mandatory");
			if (taskDetailsDto.getStartDate().isAfter(taskDetailsDto.getEndDate()))
				throw new BadRequestException("Start date should not greater than end date");

		} else
			throw new BadRequestException("Task Request is null or empty");
	}

	@Override
	public TaskDetailsDTO updateTask(TaskDetailsDTO taskDetailsDto, String taskStatus) {
		TaskDetailsDTO taskDetail = new TaskDetailsDTO();
		log.info("TaskServiceImpl:: updateTask:: entering update task method");

		validateTaskStatus(taskDetailsDto, taskStatus);
		TaskEntity taskEntity = taskRepo.findById(taskDetailsDto.getId())
				.orElseThrow(() -> new TaskNotFoundException("Task not exist with id: " + taskDetailsDto.getId()
						+ " and task name " + taskDetailsDto.getTaskName()));

		taskEntity.setTaskStatus(taskStatus);
		if (taskStatus.equals(TodoSystemConstants.COMPLETED))
			taskEntity.setActive(false);

		taskRepo.save(taskEntity);

		taskDetail = entityToDtoConvertor(taskEntity);
		
		TaskEvent taskEvent = TaskEvent.builder()
				.eventType("Update")
				.task(taskDetail)
				.build();
		kafkaPublisher.publishTaskDetails(taskEvent);
		
		return taskDetail;

	}

	private void validateTaskStatus(TaskDetailsDTO taskDetailsDto, String taskStatus) {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = taskDetailsDto.getEndDate();
		if (!status.contains(taskStatus)) {
			throw new BadRequestException("Invalid Task Status " + taskStatus);
		} else {
			if (taskStatus.equalsIgnoreCase(TodoSystemConstants.PENDING)) {
				if (now.isAfter(endDate))
					throw new BadRequestException(
							"Task end date is less than current date. can't mark it as  Pending.");
			}
			if (taskStatus.equalsIgnoreCase(TodoSystemConstants.COMPLETED)) {
				if (now.isBefore(endDate))
					throw new BadRequestException(
							"Task end date is greater than current date. can't mark it as  completed.");
			}
		}
	}

	private TaskDetailsDTO entityToDtoConvertor(TaskEntity entity) {
		TaskDetailsDTO task = new TaskDetailsDTO();
		if (entity != null) {
			task = TaskDetailsDTO.builder().id(entity.getId()).taskName(entity.getTaskName())
					.taskDescription(entity.getTaskDescription()).taskStatus(entity.getTaskStatus())
					.effort(entity.getEffort()).startDate(entity.getStartDate()).endDate(entity.getEndDate())
					.isActive(entity.isActive()).build();
		}
		return task;
	}

	private TaskEntity taskDtoToEntityConverter(TaskDetailsDTO dto) {

		return TaskEntity.builder().id(dto.getId()).taskName(dto.getTaskName().trim())
				.taskDescription(dto.getTaskDescription()).taskStatus(dto.getTaskStatus()).effort(dto.getEffort())
				.startDate(dto.getStartDate()).endDate(dto.getEndDate()).isActive(dto.isActive()).build();
	}

}
