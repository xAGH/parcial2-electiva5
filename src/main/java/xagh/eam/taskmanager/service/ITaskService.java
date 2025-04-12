package xagh.eam.taskmanager.service;

import java.util.List;
import java.util.Optional;

import xagh.eam.taskmanager.dto.TaskRequest;
import xagh.eam.taskmanager.dto.TaskUpdateRequest;
import xagh.eam.taskmanager.model.Task;

public interface ITaskService {
    List<Task> getAllTasks();
    Optional<Task> getTaskById(Long id);
    Task createTask(TaskRequest taskrequest);
    Task updateTask(Long id, TaskUpdateRequest taskrequest);
    void deleteTask(Long id);
}
