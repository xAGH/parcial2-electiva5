package xagh.eam.taskmanager.service;

import xagh.eam.taskmanager.dto.TaskRequest;
import xagh.eam.taskmanager.dto.TaskUpdateRequest;
import xagh.eam.taskmanager.model.Task;
import xagh.eam.taskmanager.model.TaskStatus;
import xagh.eam.taskmanager.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_shouldReturnTasks() {
        List<Task> tasks = List.of(Task.builder().id(1L).title("Test").build());
        when(taskRepository.findByDeletedAtIsNull()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        verify(taskRepository).findByDeletedAtIsNull();
    }

    @Test
    void getTaskById_shouldReturnTask() {
        Task task = Task.builder().id(1L).title("Sample").build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("Sample", result.get().getTitle());
    }

    @Test
    void getTaskById_shouldReturnEmptyIfDeleted() {
        Task task = Task.builder().id(1L).title("Sample").deletedAt(LocalDateTime.now()).build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void createTask_shouldReturnSavedTask() {
        TaskRequest request = new TaskRequest("New Task", "Description", TaskStatus.PENDING);
        Task task = Task.builder().title("New Task").description("Description").status(TaskStatus.PENDING).build();

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(request);

        assertEquals("New Task", result.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_shouldUpdateAndReturnTask() {
        Task existing = Task.builder().id(1L).title("Old").build();
        TaskUpdateRequest updateRequest = new TaskUpdateRequest("New", null, TaskStatus.COMPLETED);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task result = taskService.updateTask(1L, updateRequest);

        assertEquals("New", result.getTitle());
        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepository).save(existing);
    }

    @Test
    void updateTask_shouldThrowIfNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.updateTask(1L, new TaskUpdateRequest()));
    }

    @Test
    void deleteTask_shouldSetDeletedAt() {
        Task task = spy(Task.builder().id(1L).build());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(task).delete();
        verify(taskRepository).save(task);
    }

    @Test
    void updateTask_shouldUpdateDescription_whenOnlyDescriptionProvided() {

        Long taskId = 1L;
        Task existing = Task.builder()
                .id(taskId)
                .title("Old Title")
                .description("Old Description")
                .status(TaskStatus.PENDING)
                .build();

        TaskUpdateRequest updateRequest = new TaskUpdateRequest();
        updateRequest.setDescription("Updated Description");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        Task updated = taskService.updateTask(taskId, updateRequest);

        assertEquals("Old Title", updated.getTitle());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(TaskStatus.PENDING, updated.getStatus());
    }

    @Test
    void updateTask_shouldThrowIfTaskIsDeleted() {

        Long taskId = 1L;
        Task deletedTask = Task.builder()
                .id(taskId)
                .title("Deleted Task")
                .deletedAt(LocalDateTime.now())
                .build();

        TaskUpdateRequest updateRequest = new TaskUpdateRequest("New Title", null, null);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(deletedTask));

        assertThrows(RuntimeException.class, () -> taskService.updateTask(taskId, updateRequest));
        verify(taskRepository, never()).save(any());
    }
}
