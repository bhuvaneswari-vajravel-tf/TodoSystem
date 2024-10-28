package com.todo.task.system.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.todo.task.system.entity.TaskEntity;

@Repository
public interface TaskRepository extends MongoRepository<TaskEntity, String> {

	Optional<TaskEntity> findByTaskNameIgnoreCase(String taskName);

	Optional<TaskEntity> findByTaskId(Integer taskId);
}
