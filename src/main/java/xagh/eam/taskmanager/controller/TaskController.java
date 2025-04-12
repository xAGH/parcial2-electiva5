package xagh.eam.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import xagh.eam.taskmanager.dto.ApiResponse;
import xagh.eam.taskmanager.dto.TaskRequest;
import xagh.eam.taskmanager.dto.TaskUpdateRequest;
import xagh.eam.taskmanager.model.Task;
import xagh.eam.taskmanager.service.TaskService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Task>>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(new ApiResponse<>(true, "Tasks retrieved successfully", tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> getTaskById(@PathVariable("id") Long id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(new ApiResponse<>(true, "Task found", task)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Task not found", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> createTask(@RequestBody @Valid TaskRequest task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task created", created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Task>> updateTask(@PathVariable("id") Long id, @RequestBody TaskUpdateRequest task) {
        try {
            Task updated = taskService.updateTask(id, task);
            return ResponseEntity.ok(new ApiResponse<>(true, "Task updated", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, "Task not found", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Task deleted", null));
    }
}
