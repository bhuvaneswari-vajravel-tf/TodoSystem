package com.todo.task.system.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TaskDetailsDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private int id;
	private String taskName;
	private String taskDescription;
	private String taskStatus;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private int effort;
	private boolean isActive;

}
