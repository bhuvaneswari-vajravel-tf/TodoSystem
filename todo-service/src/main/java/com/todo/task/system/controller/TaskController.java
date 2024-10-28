package com.todo.task.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
//@CrossOrigin(origins = "http://localhost:4200") 
public class TaskController {
	@Autowired
	private TaskService todoService;
	
	
	@PostMapping("/add-list")
	@Operation(summary = "Add task into list")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Task Created"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "409", description = "Duplicate Task Name") })
	public ResponseEntity<TaskDetailsDTO> createTask(@RequestBody TaskDetailsDTO taskDetailDTO) throws Exception {

		log.info("TaskController :: createTask:: taskDetailDto ", taskDetailDTO);
		TaskDetailsDTO taskDetailResponse = todoService.createTask(taskDetailDTO);
		log.info("TaskController :: createTask:: taskDetailResponse ", taskDetailResponse);
		return new ResponseEntity<TaskDetailsDTO>(taskDetailResponse, HttpStatus.CREATED);

	}

	@PutMapping("/update/{taskStatus}")
	@Operation(summary = "Update Task Status")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "404", description = "Task not found") })
	public ResponseEntity<TaskDetailsDTO> updateTask(@RequestBody TaskDetailsDTO taskDetailDTO,
			@PathVariable(name = "taskStatus") String taskStatus) throws Exception {

		log.info("TaskController :: updateTask:: taskDetailDto ", taskDetailDTO);
		TaskDetailsDTO taskDetailResponse = todoService.updateTask(taskDetailDTO, taskStatus);
		log.info("TaskController :: updateTask:: taskDetailResponse ", taskDetailResponse);
		return new ResponseEntity<TaskDetailsDTO>(taskDetailResponse, HttpStatus.OK);
	}
}
