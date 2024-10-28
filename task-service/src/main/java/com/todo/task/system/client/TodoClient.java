package com.todo.task.system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.todo.task.system.dto.TaskDetailsDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(name = "todo-client", url = "${clients.endpoints.todoSystem}")
public interface TodoClient {
	
	@PutMapping("/todolist/api/v1/user/update/{taskStatus}")
	@CircuitBreaker(name = "fetchAllTasksCircuit", fallbackMethod = "updateTasksFallback")
	ResponseEntity<TaskDetailsDTO> updateTask(
            @RequestBody TaskDetailsDTO taskDetailDTO,
            @PathVariable("taskStatus") String taskStatus);

	default TaskDetailsDTO updateTasksFallback(Exception exception) {
		System.out.println("Circuit open for Todo System");
		
		return new TaskDetailsDTO();
	}
}