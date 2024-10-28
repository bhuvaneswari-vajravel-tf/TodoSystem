package com.todo.task.system.listener;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskEvent;
import com.todo.task.system.service.TaskService;

@RunWith(MockitoJUnitRunner.class)
public class TaskEventListenerTest {

    @InjectMocks
    private TaskEventListener taskEventListener;

    @Mock
    private TaskService todoService;

    private TaskEvent taskEvent;
    
    private TaskDetailsDTO task ;

    @Before
    public void setUp() {
    	
    	 task  = TaskDetailsDTO.builder()
    	            .id(1)
    	            .taskName("Test Task")
    	            .taskDescription("Description")
    	            .taskStatus("Created")
    	            .effort(5)
    	            .isActive(true)
    	            .build();
    	    
        taskEvent = TaskEvent.builder()
            .eventType("Create")
            .task(task)
            .build();
    }

    @Test
    public void testConsumeTodoMessage_CreateEvent() {
       
        // When
        taskEventListener.consumeTodoMessage(taskEvent, "task-event1", 0L);

        // Assert
        verify(todoService).processCreateTask(taskEvent.getTask());
    }

    @Test
    public void testConsumeTodoMessage_UpdateEvent() {
        // Given
        taskEvent.setEventType("Update");
        taskEvent.setTask(task);

        // When
        taskEventListener.consumeTodoMessage(taskEvent, "task-event1", 1L);

        // Assert
        verify(todoService).processUpdateTask(taskEvent.getTask());
    }

    @Test
    public void testConsumeTodoMessage_NullTask() {
        // Given
        taskEvent.setEventType("Create");
        taskEvent.setTask(null);

        // When
        taskEventListener.consumeTodoMessage(taskEvent, "task-event1", 2L);

        // Assert
        verify(todoService, never()).processCreateTask(any());
    }

    @Test
    public void testConsumeTodoMessage_OtherEventType() {
        // Given
        taskEvent.setEventType("Delete");
        taskEvent.setTask(task);

        // When
        taskEventListener.consumeTodoMessage(taskEvent, "task-event1", 3L);

        // Assert
        verify(todoService, never()).processCreateTask(any());
        verify(todoService, never()).processUpdateTask(any());
    }
}
