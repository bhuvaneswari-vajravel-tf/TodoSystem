package com.todo.task.system.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "task")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

	@Id
	private String id;
	private Integer taskId;
	private String taskName;
	private String taskDescription;
	private String taskStatus;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private int effort;
	private boolean isActive;

}
