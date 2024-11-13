package com.todo.task.system.service;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskDetailsResponse;


public interface TaskService {

	TaskDetailsResponse getAllTodos(Integer pageNo, Integer pageSize);
	
	TaskDetailsResponse fetchAllTasks();

	TaskDetailsDTO getTaskByName(String taskName);
	
	void processCreateTask(TaskDetailsDTO task);

	void processUpdateTask(TaskDetailsDTO task);

	TaskDetailsDTO updateTask(TaskDetailsDTO taskDetailDTO, String taskStatus);

}
