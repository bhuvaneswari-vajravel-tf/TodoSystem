package com.todo.task.system.publisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

import com.todo.task.system.dto.TaskDetailsDTO;
import com.todo.task.system.dto.TaskEvent;

@RunWith(MockitoJUnitRunner.class)
public class KafkaPublisherServiceTest {

    @InjectMocks
    private KafkaPublisherService kafkaPublisherService;

    @Mock
    private KafkaTemplate<String, TaskEvent> kafkaTemplate;

    private TaskEvent taskEvent;

    @Before
    public void setUp() {
        taskEvent = new TaskEvent();
        taskEvent.setEventType("Create");
        taskEvent.setTask(buildTaskDTO());
      
    }

    @Test
    public void testPublishTaskDetails() {
        // When
        kafkaPublisherService.publishTaskDetails(taskEvent);

     }

    @Test(expected = RuntimeException.class)
    public void testPublishTaskDetails_WithException() {
        // Given
        doThrow(new RuntimeException("Kafka send failed")).when(kafkaTemplate).send(any(Message.class));

        // When
        kafkaPublisherService.publishTaskDetails(taskEvent);
    }
    

	private TaskDetailsDTO buildTaskDTO() {
		LocalDateTime now =LocalDateTime.now();
		LocalDateTime start = now;
		start.withHour(now.getHour() + 1);

		LocalDateTime end = now;
		end.withDayOfMonth(now.getDayOfMonth() +1);

		return TaskDetailsDTO.builder().taskName("task1").taskDescription("taskDescription1").taskStatus("CREATED")
				.startDate(start).endDate(end).effort(10).build();
	}
}
