package com.todo.task.system.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.todo.task.system.client.TodoClient;
import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskDetailsResponse;
import com.todo.task.system.entity.TaskEntity;
import com.todo.task.system.exception.TaskNotFoundException;
import com.todo.task.system.repository.TaskRepository;
import com.todo.task.system.service.TaskService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

	@Autowired
	TodoClient todoClient;

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public TaskDetailsDTO updateTask(TaskDetailsDTO taskDetailDTO, String taskStatus) {
        ResponseEntity<TaskDetailsDTO> response = todoClient.updateTask(taskDetailDTO, taskStatus);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); 
        } else {
            throw new RuntimeException("Failed to update task: " + response.getStatusCode());
        }
    }

	@Override
	public TaskDetailsResponse getAllTodos(Integer pageNo, Integer pageSize) {

		// List<TaskEntity> taskEntity = taskRepository.findAll();
		Sort sort = Sort.by("effort").descending();

		// create Pageable instance
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		Page<TaskEntity> taskList = taskRepository.findAll(pageable);

		TaskDetailsResponse taskResponse = new TaskDetailsResponse();
		// get content for page object
		if (taskList != null && taskList.hasContent()) {
			List<TaskEntity> listOfTasks = taskList.getContent();
			List<TaskDetailsDTO> content = listOfTasks.stream().map(task -> entityToDtoConvertor(task))
					.collect(Collectors.toList());
			taskResponse.setTaskList(content);
			taskResponse.setPageNo(taskList.getNumber());
			taskResponse.setPageSize(taskList.getSize());
			taskResponse.setTotalElements(taskList.getTotalElements());
			taskResponse.setTotalPages(taskList.getTotalPages());
			taskResponse.setLast(taskList.isLast());
		}
		return taskResponse;
	}

	public TaskEntity createTodo(TaskEntity task) {
		return taskRepository.save(task);
	}

	@Override
	public TaskDetailsDTO getTaskByName(String taskName) {
		TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();

		Optional<TaskEntity> taskEntity = taskRepository.findByTaskNameIgnoreCase(taskName);

		if (!taskEntity.isPresent()) {
			throw new TaskNotFoundException("Task not found with given task name :" + taskName);
		}

		taskDetailsDTO = entityToDtoConvertor(taskEntity.get());

		return taskDetailsDTO;
	}

	@Override
	public void processCreateTask(TaskDetailsDTO task) {
		log.info("TaskServiceImpl :: processCreateTask :: started parocessing");

		taskRepository.save(taskDtoToEntityConverter(task));
		log.info("TaskServiceImpl :: processCreateTask :: task created succuessfully");

	}

	@Override
	public void processUpdateTask(TaskDetailsDTO task) {

		log.info("TaskServiceImpl :: processUpdateTask :: started parocessing");
		Optional<TaskEntity> existingTask = taskRepository.findByTaskId(task.getId());
		if (existingTask.isPresent()) {
			TaskEntity taskToUpdate = existingTask.get();
			taskToUpdate.setTaskName(task.getTaskName());
			taskToUpdate.setTaskDescription(task.getTaskDescription());
			taskToUpdate.setTaskStatus(task.getTaskStatus());
			taskRepository.save(taskToUpdate);
			log.info("TaskServiceImpl :: processUpdateTask :: updated task in db");

		} else {
			log.info("TaskServiceImpl :: processUpdateTask ::Task not found with given task Id :" + task.getId());
		}

	}

	private TaskDetailsDTO entityToDtoConvertor(TaskEntity entity) {
		TaskDetailsDTO task = new TaskDetailsDTO();
		if (entity != null) {
			task = TaskDetailsDTO.builder().id(entity.getTaskId()).taskName(entity.getTaskName())
					.taskDescription(entity.getTaskDescription()).taskStatus(entity.getTaskStatus())
					.effort(entity.getEffort()).startDate(entity.getStartDate()).endDate(entity.getEndDate())
					.isActive(entity.isActive()).build();
		}
		return task;
	}

	private TaskEntity taskDtoToEntityConverter(TaskDetailsDTO dto) {

		return TaskEntity.builder().taskId(dto.getId()).taskName(dto.getTaskName())
				.taskDescription(dto.getTaskDescription()).taskStatus(dto.getTaskStatus()).effort(dto.getEffort())
				.startDate(dto.getStartDate()).endDate(dto.getEndDate()).isActive(dto.isActive()).build();
	}

}