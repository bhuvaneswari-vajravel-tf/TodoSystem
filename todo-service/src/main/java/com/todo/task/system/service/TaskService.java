package com.todo.task.system.service;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskDetailsResponse;

public interface TaskService {

	TaskDetailsDTO createTask(TaskDetailsDTO taskDetailsDto);

	TaskDetailsDTO updateTask(TaskDetailsDTO taskDetailDTO, String taskStatus);

//	TaskDetailsDTO getTaskByName(String taskName);
//
//	TaskDetailsResponse getAllTask(Integer pageNo, Integer pageSize);
//	
//	TaskDetailsResponse fetchAllTask();


}