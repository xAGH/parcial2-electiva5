package xagh.eam.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import xagh.eam.taskmanager.dto.TaskRequest;
import xagh.eam.taskmanager.dto.TaskUpdateRequest;
import xagh.eam.taskmanager.model.Task;
import xagh.eam.taskmanager.model.TaskStatus;
import xagh.eam.taskmanager.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    void testCreateTaskIntegration() throws Exception {
        TaskRequest task = new TaskRequest();
        task.setTitle("Tarea de integración");
        task.setDescription("Probando integración");
        task.setStatus(TaskStatus.PENDING);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.title", is("Tarea de integración")));
    }

    @Test
    void testGetAllTasksIntegration() throws Exception {
        Task task = new Task();
        task.setTitle("Tarea 1");
        task.setDescription("Desc 1");
        taskRepository.save(task);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title", is("Tarea 1")));
    }

    @Test
    void testGetTaskByIdFoundIntegration() throws Exception {
        Task task = new Task();
        task.setTitle("Consulta por ID");
        task.setDescription("Desc");
        Task saved = taskRepository.save(task);

        mockMvc.perform(get("/api/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Consulta por ID")));
    }

    @Test
    void testGetTaskByIdNotFoundIntegration() throws Exception {
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    void testUpdateTaskIntegration() throws Exception {
        Task task = new Task();
        task.setTitle("Tarea original");
        task.setDescription("Desc");
        Task saved = taskRepository.save(task);

        TaskUpdateRequest update = new TaskUpdateRequest();
        update.setTitle("Actualizada");

        mockMvc.perform(patch("/api/tasks/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Actualizada")));
    }

    @Test
    void testUpdateTaskNotFoundIntegration() throws Exception {
        TaskUpdateRequest update = new TaskUpdateRequest();
        update.setTitle("No existe");

        mockMvc.perform(patch("/api/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTaskIntegration() throws Exception {
        Task task = new Task();
        task.setTitle("Eliminarme");
        task.setDescription("bye");
        Task saved = taskRepository.save(task);

        mockMvc.perform(delete("/api/tasks/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Task deleted")));

        mockMvc.perform(get("/api/tasks/" + saved.getId()))
                .andExpect(status().isNotFound());
    }
}
