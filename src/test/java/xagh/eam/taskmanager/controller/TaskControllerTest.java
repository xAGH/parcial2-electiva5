package xagh.eam.taskmanager.controller;

import xagh.eam.taskmanager.dto.*;
import xagh.eam.taskmanager.model.Task;
import xagh.eam.taskmanager.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        Task task = new Task();
        when(taskService.getAllTasks()).thenReturn(List.of(task));

        ResponseEntity<ApiResponse<List<Task>>> response = taskController.getAllTasks();

        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testGetTaskByIdFound() {
        Task task = new Task();
        task.setId(1L);
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        ResponseEntity<ApiResponse<Task>> response = taskController.getTaskById(1L);

        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
        assertEquals(1L, response.getBody().getData().getId());
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Task>> response = taskController.getTaskById(1L);

        assertFalse(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test
    void testCreateTask() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Nueva tarea");
        request.setDescription("Descripción");

        Task createdTask = new Task();
        createdTask.setTitle(request.getTitle());
        createdTask.setDescription(request.getDescription());

        when(taskService.createTask(request)).thenReturn(createdTask);

        ResponseEntity<ApiResponse<Task>> response = taskController.createTask(request);

        assertTrue(response.getBody().isSuccess());
        assertEquals("Task created", response.getBody().getMessage());
        assertEquals(request.getTitle(), response.getBody().getData().getTitle());
    }

    @Test
    void testUpdateTaskNotFound() {
        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setTitle("Actualizado");

        when(taskService.updateTask(1L, updateRequest)).thenThrow(new RuntimeException("Task not found"));

        ResponseEntity<ApiResponse<Task>> response = taskController.updateTask(1L, updateRequest);

        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getStatusCode().equals(HttpStatus.NOT_FOUND));
        assertEquals("Task not found", response.getBody().getMessage());
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<ApiResponse<Void>> response = taskController.deleteTask(1L);

        assertTrue(response.getBody().isSuccess());
        assertEquals("Task deleted", response.getBody().getMessage());
        verify(taskService, times(1)).deleteTask(1L);
    }
}
