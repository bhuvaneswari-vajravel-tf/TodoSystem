package com.todo.task.system.service.impl;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskEvent;
import com.todo.task.system.entity.TaskEntity;
import com.todo.task.system.exception.BadRequestException;
import com.todo.task.system.exception.DuplicateTaskNameException;
import com.todo.task.system.exception.TaskNotFoundException;
import com.todo.task.system.publisher.KafkaPublisherService;
import com.todo.task.system.repository.TaskRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

	@InjectMocks
	TaskServiceImpl taskService;

	@Mock
	KafkaPublisherService kafkaPublisher;

	@Mock
	TaskRepository taskRepo;

	String task1 = "task1";

	LocalDateTime now = LocalDateTime.now();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreateTask() {

		// Given
		TaskDetailsDTO taskDTO = buildTaskDTO();
		when(taskRepo.findByTaskNameIgnoreCase(task1)).thenReturn(Optional.empty());
		TaskEvent event = TaskEvent.builder().eventType("Create").task(taskDTO).build();
     //   doNothing().when(kafkaPublisher).publishTaskDetails(event);

		// when
		taskService.createTask(taskDTO);

	}

	@Test(expected = DuplicateTaskNameException.class)
	public void testCreateTaskDuplicateTaskException() {

		// Given
		TaskEntity taskEntity = buildTask();
		TaskDetailsDTO taskDTO = buildTaskDTO();
		when(taskRepo.findByTaskNameIgnoreCase(task1)).thenReturn(Optional.of(taskEntity));

		// when

		taskService.createTask(taskDTO);

	}

	@Test
	public void testUpdateTaskStatusPending() {

		// Given
		TaskEntity taskEntity = buildTask();
		TaskDetailsDTO taskDTO = buildTaskDTO();
		taskDTO.setId(1);
		taskDTO.setEndDate(now.plusDays(1));
		when(taskRepo.findById(taskDTO.getId())).thenReturn(Optional.of(taskEntity));
		TaskEvent event = TaskEvent.builder().eventType("Update").task(taskDTO).build();
      //  doNothing().when(kafkaPublisher).publishTaskDetails(event);

		// when
		taskService.updateTask(taskDTO, "PENDING");
	}

	@Test(expected = BadRequestException.class)
	public void testUpdateTaskStatusPendingException() {

		// Given
		TaskDetailsDTO taskDTO = buildTaskDTO();
		taskDTO.setId(1);
		taskDTO.setEndDate(now.minusDays(2));

		// when

		taskService.updateTask(taskDTO, "PENDING");

	}

	@Test
	public void testUpdateTaskStatusCompleted() {

		// Given
		TaskEntity taskEntity = buildTask();
		TaskDetailsDTO taskDTO = buildTaskDTO();
		taskDTO.setId(1);
		taskDTO.setEndDate(now);
		when(taskRepo.findById(taskDTO.getId())).thenReturn(Optional.of(taskEntity));

		// when
		taskService.updateTask(taskDTO, "COMPLETED");
	}

	@Test(expected = BadRequestException.class)
	public void testUpdateTaskStatusInvalidException() {

		// Given
		TaskDetailsDTO taskDTO = buildTaskDTO();
		taskDTO.setId(1);

		// when
		taskService.updateTask(taskDTO, "COMPLETE");

	}

	@Test(expected = TaskNotFoundException.class)
	public void testUpdateTaskStatusNOTFOUNDException() {

		// Given
		TaskDetailsDTO taskDTO = buildTaskDTO();
		taskDTO.setId(1);
		taskDTO.setEndDate(now.plusDays(2));

		// when
		taskService.updateTask(taskDTO, "PENDING");

	}

	@Test(expected = BadRequestException.class)
	public void testUpdateTaskStatusCompletedException() {

		// Given
		TaskDetailsDTO taskDTO = buildTaskDTO();
		taskDTO.setId(1);
		taskDTO.setEndDate(now.plusDays(1));

		// when

		taskService.updateTask(taskDTO, "COMPLETED");

	}

	@Test(expected = BadRequestException.class)
	public void testValidateTask_NullTaskDetails() {
		// When
		taskService.createTask(null);
	}

	@Test(expected = BadRequestException.class)
	public void testValidateTask_EmptyTaskName() {
		// When
		TaskDetailsDTO taskDetailsDto = new TaskDetailsDTO();
		taskDetailsDto.setTaskName("");

		// Given
		taskService.createTask(taskDetailsDto);
	}

	@Test(expected = BadRequestException.class)
	public void testValidateTask_NullTaskDescription() {
		// Given
		TaskDetailsDTO taskDetailsDto = new TaskDetailsDTO();
		taskDetailsDto.setTaskName("Test Task");
		taskDetailsDto.setTaskDescription(null);

		// When
		taskService.createTask(taskDetailsDto);
	}

	@Test(expected = BadRequestException.class)
	public void testValidateTask_InvalidEffort() {
		// Given
		TaskDetailsDTO taskDetailsDto = new TaskDetailsDTO();
		taskDetailsDto.setTaskName("Test Task");
		taskDetailsDto.setTaskDescription("Valid Description");
		taskDetailsDto.setEffort(-1);

		// When
		taskService.createTask(taskDetailsDto);
	}

	@Test(expected = BadRequestException.class)
	public void testValidateTask_StartDateBeforeNow() {
		// Given
		TaskDetailsDTO taskDetailsDto = new TaskDetailsDTO();
		taskDetailsDto.setTaskName("Test Task");
		taskDetailsDto.setTaskDescription("Valid Description");
		taskDetailsDto.setEffort(5);
		taskDetailsDto.setStartDate(now.withHour(now.getHour() - 1));

		// when
		taskService.createTask(taskDetailsDto);
	}

	@Test(expected = BadRequestException.class)
	public void testValidateTask_EndDateBeforeNow() {
		// Given
		TaskDetailsDTO taskDetailsDto = new TaskDetailsDTO();
		taskDetailsDto.setTaskName("Test Task");
		taskDetailsDto.setTaskDescription("Valid Description");
		taskDetailsDto.setEffort(5);
		taskDetailsDto.setEndDate(now.withDayOfMonth(now.getDayOfMonth() - 1));

		// When
		taskService.createTask(taskDetailsDto);
	}

	@Test(expected = BadRequestException.class)
	public void testValidateTask_StartDateAfterEndDate() {
		// Given
		TaskDetailsDTO taskDetailsDto = new TaskDetailsDTO();
		taskDetailsDto.setTaskName("Test Task");
		taskDetailsDto.setTaskDescription("Valid Description");
		taskDetailsDto.setEffort(5);
		taskDetailsDto.setStartDate(now.withDayOfMonth(now.getDayOfMonth() + 1));

		// When
		taskService.createTask(taskDetailsDto);
	}

	@Test
	public void testValidateTask_ValidTaskDetails() {
		// Given
		TaskDetailsDTO taskDetailsDto = new TaskDetailsDTO();
		taskDetailsDto.setTaskName("Test Task");
		taskDetailsDto.setTaskDescription("Valid Description");
		taskDetailsDto.setEffort(5);
		taskDetailsDto.setStartDate(now.withDayOfMonth(now.getDayOfMonth() + 1));
		taskDetailsDto.setEndDate(now.withDayOfMonth(now.getDayOfMonth() + 2));

		// when
		taskService.createTask(taskDetailsDto); // No exception should be thrown
	}

	private List<TaskEntity> buildTaskEntity() {
		List<TaskEntity> taskList = new ArrayList<TaskEntity>();
		TaskEntity task = buildTask();
		taskList.add(task);
		return taskList;
	}

	private TaskEntity buildTask() {
		LocalDateTime start = now;
		start.withDayOfMonth(now.getDayOfMonth() + 1);

		LocalDateTime end = now;
		end.withDayOfMonth(now.getDayOfMonth() + 2);
		return TaskEntity.builder().id(1).taskName(task1).taskDescription("taskDescription1").taskStatus("CREATED")
				.startDate(start).endDate(end).effort(10).build();
	}

	private TaskDetailsDTO buildTaskDTO() {
		LocalDateTime start = now;
		start.withDayOfMonth(now.getDayOfMonth() + 1);
		LocalDateTime end = now;
		end.withDayOfMonth(now.getDayOfMonth() + 2);

		return TaskDetailsDTO.builder().taskName(task1).taskDescription("taskDescription1").taskStatus("CREATED")
				.startDate(start).endDate(end).effort(10).build();
	}
}
