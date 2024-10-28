package com.todo.task.system.controller;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.service.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

	@InjectMocks
	TaskController taskController;

	@Mock
	TaskService taskService;

	String task1 = "task1";

//	@Test
//	public void testGetTaskByName() {
//
//		// Given
//		TaskDetailsDTO task = buildTaskDTO();
//
//		when(taskService.getTaskByName(task1)).thenReturn(task);
//
//		// when
//		taskController.getTaskByName(task1);
//
//	}
//	
//
//	@Test
//	public void testGetAllTaskWithPagination() {
//
//		// Given
//		TaskDetailsResponse taskResponse = new TaskDetailsResponse();
//		List<TaskDetailsDTO> task = new ArrayList<>();
//		task.add(buildTaskDTO());
//		taskResponse.setTaskList(task);
//
//		when(taskService.getAllTask(1, 5)).thenReturn(taskResponse);
//
//		// when
//		taskController.getAllTask(1, 5);
//
//	}
//	
//
//	@Test
//	public void testGetAllTask() {
//
//		// Given
//		TaskDetailsResponse taskResponse = new TaskDetailsResponse();
//		List<TaskDetailsDTO> task = new ArrayList<>();
//		task.add(buildTaskDTO());
//		taskResponse.setTaskList(task);
//
//		when(taskService.fetchAllTask()).thenReturn(taskResponse);
//
//		// when
//		taskController.getAllTask(0,0);
//
//	}

	@Test
	public void testCreateTask() throws Exception {

		// Given
		TaskDetailsDTO taskDTO = buildTaskDTO();

		when(taskService.createTask(taskDTO)).thenReturn(taskDTO);

		// when
		taskController.createTask(taskDTO);

	}

	@Test
	public void testUpdateTaskStatusPending() throws Exception {

		// Given
		TaskDetailsDTO taskDTO = buildTaskDTO();
		taskDTO.setId(1);
		when(taskService.updateTask(taskDTO, "PENDING")).thenReturn(taskDTO);

		// when
		taskController.updateTask(taskDTO, "PENDING");
	}


	private TaskDetailsDTO buildTaskDTO() {
		LocalDateTime now =LocalDateTime.now();
		LocalDateTime start = now;
		start.withHour(now.getHour() + 1);

		LocalDateTime end = now;
		end.withDayOfMonth(now.getDayOfMonth() +1);

		return TaskDetailsDTO.builder().taskName(task1).taskDescription("taskDescription1").taskStatus("CREATED")
				.startDate(start).endDate(end).effort(10).build();
	}
}
