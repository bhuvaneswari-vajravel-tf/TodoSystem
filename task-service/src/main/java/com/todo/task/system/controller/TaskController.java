package com.todo.task.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskDetailsResponse;
import com.todo.task.system.service.TaskService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/tasklist")
public class TaskController {

	@Autowired
	private TaskService todoService;

	@PutMapping("update/task/{taskStatus}")
	public ResponseEntity<TaskDetailsDTO> updateTask(@RequestBody TaskDetailsDTO taskDetailDTO,
			@PathVariable(name = "taskStatus") String taskStatus) throws Exception {

		log.info("TaskController :: updateTask:: taskDetailDto ", taskDetailDTO);
		TaskDetailsDTO taskDetailResponse = todoService.updateTask(taskDetailDTO, taskStatus);
		log.info("TaskController :: updateTask:: taskDetailResponse ", taskDetailResponse);
		return new ResponseEntity<TaskDetailsDTO>(taskDetailResponse, HttpStatus.OK);
	}

	@GetMapping("/list/task/all")
	public ResponseEntity<TaskDetailsResponse> getAllTodos(
		 @RequestParam(value = "pageNo", defaultValue ="0", required = false) Integer pageNo,
		 @RequestParam(value = "pageSize", defaultValue ="5", required = false) Integer pageSize) {
		log.info("TaskController :: getAllTask::fetching All task details");
		return new ResponseEntity<TaskDetailsResponse>(todoService.getAllTodos(pageNo, pageSize), HttpStatus.OK);

	}
	
	@GetMapping("/list/{taskName}")
	public ResponseEntity<TaskDetailsDTO> getTaskByName(@PathVariable(name = "taskName") String taskName) {

		log.info("TaskController :: fetchTaskByName::fetching task details by Task name");
		TaskDetailsDTO taskDetails = todoService.getTaskByName(taskName);
		return new ResponseEntity<TaskDetailsDTO>(taskDetails, HttpStatus.OK);
	}

}
