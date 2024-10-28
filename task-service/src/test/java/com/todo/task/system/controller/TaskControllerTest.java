package com.todo.task.system.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskDetailsResponse;
import com.todo.task.system.service.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService todoService;

    private MockMvc mockMvc;

    private TaskDetailsDTO taskDetailsDTO;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        taskDetailsDTO = TaskDetailsDTO.builder()
            .id(1)
            .taskName("Test Task")
            .taskDescription("Description")
            .taskStatus("Created")
            .effort(5)
            .isActive(true)
            .build();
    }

    @Test
    public void testUpdateTask() throws Exception {
        // Given
        when(todoService.updateTask(any(TaskDetailsDTO.class), anyString())).thenReturn(taskDetailsDTO);

        // When & Assert
        mockMvc.perform(put("/tasklist/update/task/Updated")
                .contentType("application/json")
                .content("{\"id\":\"1\",\"taskName\":\"Test Task\",\"taskDescription\":\"Description\",\"taskStatus\":\"Updated\",\"effort\":5,\"isWhenive\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("Test Task"));

        verify(todoService).updateTask(any(TaskDetailsDTO.class), eq("Updated"));
    }

    @Test
    public void testGetAllTodos() throws Exception {
        // Given
        TaskDetailsResponse taskResponse = new TaskDetailsResponse();
        taskResponse.setTaskList(Arrays.asList(taskDetailsDTO));
        when(todoService.getAllTodos(0, 5)).thenReturn(taskResponse);

        // When & Assert
        mockMvc.perform(get("/tasklist/list/task/all?pageNo=0&pageSize=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskList[0].taskName").value("Test Task"));

        verify(todoService).getAllTodos(0, 5);
    }

    @Test
    public void testGetTaskByName() throws Exception {
        // Given
        when(todoService.getTaskByName("Test Task")).thenReturn(taskDetailsDTO);

        // When & Assert
        mockMvc.perform(get("/tasklist/list/Test Task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskName").value("Test Task"));

        verify(todoService).getTaskByName("Test Task");
    }
}
