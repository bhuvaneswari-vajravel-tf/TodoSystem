package com.todo.task.system.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="task",schema="todo-service")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
    @Column(nullable = false,unique=true)
	private String taskName;
	private String taskDescription;
	private String taskStatus;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private int effort;
	private boolean isActive; 
	
	
}
