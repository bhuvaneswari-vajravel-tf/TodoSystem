package com.todo.task.system.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.todo.task.system.client.TodoClient;
import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskDetailsResponse;
import com.todo.task.system.entity.TaskEntity;
import com.todo.task.system.exception.TaskNotFoundException;
import com.todo.task.system.repository.TaskRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TodoClient todoClient;

    @Mock
    private TaskRepository taskRepository;

    private TaskDetailsDTO taskDetailsDTO;
    private TaskEntity taskEntity;

    @Before
    public void setUp() {
        taskDetailsDTO = TaskDetailsDTO.builder()
            .id(1)
            .taskName("Test Task")
            .taskDescription("Description")
            .taskStatus("Created")
            .effort(5)
            .isActive(true)
            .build();

        taskEntity = TaskEntity.builder()
            .taskId(1)
            .taskName("Test Task")
            .taskDescription("Description")
            .taskStatus("Created")
            .effort(5)
            .isActive(true)
            .build();
    }

    @Test
    public void testUpdateTask_Success() {
        // Given
        ResponseEntity<TaskDetailsDTO> responseEntity = new ResponseEntity<>(taskDetailsDTO, HttpStatus.OK);
        when(todoClient.updateTask(taskDetailsDTO, "Created")).thenReturn(responseEntity);

        // when
        TaskDetailsDTO result = taskService.updateTask(taskDetailsDTO, "Created");

        // Assert
        assertNotNull(result);
        assertEquals(taskDetailsDTO.getTaskName(), result.getTaskName());
        verify(todoClient).updateTask(taskDetailsDTO, "Created");
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateTask_Failure() {
        // Given
        ResponseEntity<TaskDetailsDTO> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(todoClient.updateTask(taskDetailsDTO, "Created")).thenReturn(responseEntity);

        // when
        taskService.updateTask(taskDetailsDTO, "Created");
    }

    @Test
    public void testGetAllTodos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("effort").descending());
        Page<TaskEntity> taskPage = mock(Page.class);
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        when(taskPage.hasContent()).thenReturn(true);
        when(taskPage.getContent()).thenReturn(Arrays.asList(taskEntity));
        when(taskPage.getNumber()).thenReturn(0);
        when(taskPage.getSize()).thenReturn(10);
        when(taskPage.getTotalElements()).thenReturn(1L);
        when(taskPage.getTotalPages()).thenReturn(1);
        when(taskPage.isLast()).thenReturn(true);

        // when
        TaskDetailsResponse response = taskService.getAllTodos(0, 10);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        verify(taskRepository).findAll(pageable);
    }

    @Test
    public void testGetTaskByName_Success() {
        // Given
        when(taskRepository.findByTaskNameIgnoreCase("Test Task")).thenReturn(Optional.of(taskEntity));

        // when
        TaskDetailsDTO result = taskService.getTaskByName("Test Task");

        // Assert
        assertNotNull(result);
        assertEquals(taskDetailsDTO.getTaskName(), result.getTaskName());
    }

    @Test(expected = TaskNotFoundException.class)
    public void testGetTaskByName_NotFound() {
        // Given
        when(taskRepository.findByTaskNameIgnoreCase("Nonexistent Task")).thenReturn(Optional.empty());

        // when
        taskService.getTaskByName("Nonexistent Task");
    }

    @Test
    public void testProcessCreateTask() {
        // when
        taskService.processCreateTask(taskDetailsDTO);

        // Assert
        verify(taskRepository).save(taskEntity);
    }

    @Test
    public void testProcessUpdateTask_Success() {
        // Given
        when(taskRepository.findByTaskId(1)).thenReturn(Optional.of(taskEntity));

        // when
        taskService.processUpdateTask(taskDetailsDTO);

        // Assert
        verify(taskRepository).save(taskEntity);
    }

    @Test
    public void testProcessUpdateTask_NotFound() {
        // Given
        when(taskRepository.findByTaskId(1)).thenReturn(Optional.empty());

        // when
        taskService.processUpdateTask(taskDetailsDTO);

        // Assert
        verify(taskRepository, never()).save(any());
    }
}
