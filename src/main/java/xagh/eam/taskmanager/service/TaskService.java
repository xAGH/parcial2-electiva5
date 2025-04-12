package xagh.eam.taskmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xagh.eam.taskmanager.dto.TaskRequest;
import xagh.eam.taskmanager.dto.TaskUpdateRequest;
import xagh.eam.taskmanager.model.Task;
import xagh.eam.taskmanager.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findByDeletedAtIsNull();
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id).filter(task -> !task.isDeleted());
    }

    @Override
    public Task createTask(TaskRequest taskRequest) {
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus())
                .build();
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskUpdateRequest updatedTask) {
        Task existing = Optional.of(taskRepository.findById(id))
                .flatMap(opt -> opt.filter(task -> !task.isDeleted()))
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Optional.ofNullable(updatedTask.getTitle())
                .ifPresent(existing::setTitle);

        Optional.ofNullable(updatedTask.getDescription())
                .ifPresent(existing::setDescription);

        Optional.ofNullable(updatedTask.getStatus())
                .ifPresent(existing::setStatus);

        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.delete();
            taskRepository.save(task);
        });
    }
}
